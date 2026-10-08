# 26-27 BioBuzz Season

Robot code for the 2026-2027 BioBuzz season.

## Branch Structure

We use a PI (Program Interval) based development system.

```text
main
 └── PI1
      ├── launcher
      ├── intake
      ├── transfer
      ├── drivetrain
      └── vision
```

### Branch Rules

- `main` contains the stable robot code.
- Each PI (`PI1`, `PI2`, etc.) is created from `main`.
- Subsystem branches are created from the current PI.
- Develop and push freely to your own subsystem branch.
- Do **not** develop directly on `PI1` or `main`.
- Subsystem changes are merged into the current PI through a Pull Request.
- Completed PIs are merged into `main` through a Pull Request.
- Delete subsystem branches after they are merged.

**Merge flow:** `subsystem → PI → main`

---

# Development Workflow

## 1. Work on Your Subsystem Branch

Make sure you are on the correct branch before making changes.

```bash
git checkout launcher
```

If you are working on your own subsystem branch, regularly commit and push your work:

```bash
git add .
git commit -m "Add launcher velocity control"
git push
```

Before pushing, make sure your code builds, works as expected, and doesn't contain unnecessary debug code or unrelated changes.

## 2. Format Your Code

Before committing, run Spotless.

**Windows:**
```powershell
.\gradlew spotlessApply
```

**macOS / Linux:**
```bash
./gradlew spotlessApply
```

To check formatting without changing files:

**Windows:**
```powershell
.\gradlew spotlessCheck
```

**macOS / Linux:**
```bash
./gradlew spotlessCheck
```

GitHub also runs `spotlessCheck` automatically on Pull Requests.

---

# Pull Requests

When your subsystem is ready, open a Pull Request into the **current PI branch**.

```text
launcher ──────┐
intake ────────┤
transfer ──────┼──> PI1 ──> main
drivetrain ────┤
vision ────────┘
```

Before merging:

- Explain what you changed in the PR.
- Make sure required GitHub checks pass.
- Get a review if required.
- Make sure the code is ready to be integrated.

After the PR is merged, **delete the subsystem branch**.

---
