package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BallChaseController {

	public enum State {
		IDLE, SEARCHING, CHASING, COASTING, PICKUP, DONE
	}

	private final BallTracker tracker;
	private final DcMotor lf, rf, lb, rb;
	private final DcMotor intake;

	private State state = State.IDLE;
	private int pickups = 0;
	private int failedPickups = 0;
	private int maxPickups = 0;
	private PickupConfirmer pickConfirm = null;

	private final ElapsedTime lastSeen = new ElapsedTime();
	private final ElapsedTime pickupTimer = new ElapsedTime();
	private double lastSeenDist = Double.MAX_VALUE;

	private final ElapsedTime searchClock = new ElapsedTime();
	private double searchSwept = 0;
	private boolean searchingWas = false;

	public BallChaseController(BallTracker tracker, DcMotor lf, DcMotor rf, DcMotor lb, DcMotor rb,
			DcMotor intakeOrNull) {
		this.tracker = tracker;
		this.lf = lf;
		this.rf = rf;
		this.lb = lb;
		this.rb = rb;
		this.intake = intakeOrNull;
	}

	public static void configureMecanumDirections(DcMotor lf, DcMotor rf, DcMotor lb, DcMotor rb) {
		lf.setDirection(DcMotorSimple.Direction.FORWARD);
		lb.setDirection(DcMotorSimple.Direction.FORWARD);
		rf.setDirection(DcMotorSimple.Direction.REVERSE);
		rb.setDirection(DcMotorSimple.Direction.REVERSE);
	}

	public void setMaxPickups(int n) {
		maxPickups = n;
	}

	public int getPickups() {
		return pickups;
	}

	public int getFailedPickups() {
		return failedPickups;
	}

	public State getState() {
		return state;
	}

	public boolean isDone() {
		return state == State.DONE;
	}

	public boolean isActive() {
		return state != State.IDLE && state != State.DONE;
	}

	public void setPickupConfirmer(PickupConfirmer c) {
		pickConfirm = c;
	}

	public void start() {
		pickups = 0;
		failedPickups = 0;
		tracker.reset();
		searchSwept = 0;
		searchingWas = false;
		state = State.SEARCHING;
	}

	public void abort() {
		drive(0, 0, 0);
		setIntake(0);
		tracker.reset();
		state = State.IDLE;
	}

	public void update() {
		if (!isActive())
			return;

		if (state == State.PICKUP) {
			doPickup();
			return;
		}

		BallTracker.Sighting s = tracker.update();
		if (s == null) {
			noTarget();
			return;
		}

		searchSwept = 0;
		searchingWas = false;
		double tx = s.txDeg;

		if (!s.predicted) {
			lastSeen.reset();
			lastSeenDist = s.distIn;
		} else {
			boolean wasClose = lastSeenDist <= HiveConfig.COAST_MAX_DIST;
			if (wasClose) {
				if (lastSeen.milliseconds() < HiveConfig.COAST_MS) {
					state = State.COASTING;
					setIntake(HiveConfig.INTAKE_POWER);
					drive(HiveConfig.COAST_POWER, 0, 0);
					return;
				}
				state = State.PICKUP;
				pickupTimer.reset();
				drive(0, 0, 0);
				return;
			}

			if (lastSeen.milliseconds() > HiveConfig.PREDICT_TURN_MS) {
				drive(0, 0, 0);
				return;
			}

			double turn = Range.clip(tx * HiveConfig.TURN_KP, -HiveConfig.MAX_TURN, HiveConfig.MAX_TURN);
			if (Math.abs(turn) < HiveConfig.MIN_TURN)
				turn = Math.copySign(HiveConfig.MIN_TURN, tx);
			drive(0, 0, turn);
			return;
		}

		boolean aimed = Math.abs(tx) < HiveConfig.AIM_TOL_DEG;
		if (s.distIn <= HiveConfig.STOP_DIST && aimed) {
			state = State.PICKUP;
			pickupTimer.reset();
			drive(0, 0, 0);
			return;
		}

		double turn = Range.clip(tx * HiveConfig.TURN_KP, -HiveConfig.MAX_TURN, HiveConfig.MAX_TURN);
		double fwd = 0.0;
		if (s.distIn > HiveConfig.STOP_DIST && Math.abs(tx) < HiveConfig.DRIVE_MIN_TX) {
			fwd = Range.clip((s.distIn - HiveConfig.STOP_DIST) * HiveConfig.DRIVE_KP, HiveConfig.MIN_FWD,
					HiveConfig.MAX_FWD);
		}

		state = State.CHASING;
		setIntake(HiveConfig.INTAKE_POWER);
		drive(fwd, 0, turn);
	}

	public void addTelemetry(Telemetry t) {
		BallTracker.Sighting s = tracker.getLast();
		t.addData("chase", "%s pickups=%d/%s failed=%d", state, pickups,
				maxPickups > 0 ? Integer.toString(maxPickups) : "unlimited", failedPickups);
		if (s != null) {
			t.addData("chase target", "tx=%+.1f ty=%+.1f dist=%.1f in cls=%d conf=%.2f%s", s.txDeg, s.tyDeg, s.distIn,
					s.classId, s.confidence, s.predicted ? " (predicted)" : "");
		} else {
			t.addData("chase target", "none");
		}
	}

	private void doPickup() {
		drive(0, 0, 0);
		setIntake(HiveConfig.INTAKE_POWER);
		if (pickupTimer.milliseconds() < HiveConfig.PICKUP_DWELL_MS)
			return;

		boolean confirmed = pickConfirm == null || pickConfirm.isBallInIntake();
		if (!confirmed) {
			if (HiveConfig.PICKUP_CONFIRM_MS <= 0
					|| pickupTimer.milliseconds() >= HiveConfig.PICKUP_DWELL_MS + HiveConfig.PICKUP_CONFIRM_MS) {
				failedPickups++;
				tracker.reset();
				state = State.SEARCHING;
			}
			return;
		}

		pickups++;
		tracker.reset();
		if (maxPickups > 0 && pickups >= maxPickups) {
			setIntake(0);
			state = State.DONE;
		} else {
			state = State.SEARCHING;
		}
	}

	private void noTarget() {
		boolean wasClose = (state == State.CHASING || state == State.COASTING)
				&& lastSeenDist <= HiveConfig.COAST_MAX_DIST;
		if (wasClose) {
			if (lastSeen.milliseconds() < HiveConfig.COAST_MS) {
				state = State.COASTING;
				setIntake(HiveConfig.INTAKE_POWER);
				drive(HiveConfig.COAST_POWER, 0, 0);
			} else {
				state = State.PICKUP;
				pickupTimer.reset();
				drive(0, 0, 0);
			}
			return;
		}

		state = State.SEARCHING;
		searchTurn();
	}

	private void searchTurn() {
		if (searchingWas) {
			double dt = Math.min(searchClock.seconds(), 0.1);
			searchSwept += HiveConfig.SEARCH_TURN * HiveConfig.TURN_RATE_RAD_PER_POWER_SEC * dt;
			if (searchSwept >= Math.toRadians(HiveConfig.SEARCH_SWEEP_DEG)) {
				setIntake(0);
				drive(0, 0, 0);
				state = State.DONE;
				return;
			}
		}
		searchClock.reset();
		searchingWas = true;
		setIntake(0);
		drive(0, 0, HiveConfig.SEARCH_TURN);
	}

	private void drive(double fwd, double strafe, double turn) {
		if (lf == null || rf == null || lb == null || rb == null)
			return;
		double pLf = fwd + strafe + turn;
		double pRf = fwd - strafe - turn;
		double pLb = fwd - strafe + turn;
		double pRb = fwd + strafe - turn;

		double max = Math.max(1.0,
				Math.max(Math.max(Math.abs(pLf), Math.abs(pRf)), Math.max(Math.abs(pLb), Math.abs(pRb))));
		lf.setPower(pLf / max);
		rf.setPower(pRf / max);
		lb.setPower(pLb / max);
		rb.setPower(pRb / max);
	}

	private void setIntake(double power) {
		if (intake != null)
			intake.setPower(power);
	}
}
