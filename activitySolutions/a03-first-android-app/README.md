# A03: First Android App

## Overview

In this assignment, you will practice the workflow used to complete and submit Android Studio projects in this course. You will:

- Accept a Classroom 50 assignment.
- Clone an existing Android Studio project from GitHub.
- Build and run a Jetpack Compose app.
- Make two small changes to the app.
- Commit those changes using Android Studio.
- Push your commits to GitHub.

The starter project is already configured. **Do not create a new Android Studio project for this assignment.**

## Before You Begin

You will need:

- A GitHub account enrolled in the course organization.
- Android Studio installed and configured.
- Git installed on your computer.
- **[Optional]** An Android emulator or physical Android device on which you can run the app.

## 1. Accept the Assignment

1. Open the Classroom 50 assignment link provided in Pilot.
2. Sign in with the GitHub account you use for this course.
3. Select **Accept assignment**.
4. Wait while Classroom 50 creates your repository.
5. Select **Open repository** when setup is complete.

Your repository is your personal copy of the assignment. Make sure the repository name ends with your GitHub username.

## 2. Connect Android Studio to GitHub

If your GitHub account is not already connected to Android Studio:

1. Open Android Studio.
2. Open **Settings**.
3. Select **Version Control > GitHub**.
4. Select **Add account** and then **Log In via GitHub**.
5. Complete the sign-in process in your browser.
6. Return to Android Studio when authorization is complete.

Use **Log In via GitHub** unless your instructor directs you to use a personal access token. See [Personal Access Token Troubleshooting](#personal-access-token-troubleshooting) if browser login does not work.

## 3. Clone the Starter Project

1. On the Android Studio welcome screen, select **Clone Repository** or **Get from VCS**.
   - If another project is already open, select **File > New > Project from Version Control**.
2. Select **GitHub** and choose your assignment repository.
   - If the repository does not appear, select the URL option and paste the HTTPS URL from your repository's GitHub page.
3. Choose a local directory for the project.
4. Select **Clone**.
5. If Android Studio asks whether you trust the project, verify that it is your Classroom 50 repository and select **Trust Project**.
6. Wait for Gradle synchronization and project indexing to finish.

On Windows, clone the project into a normal local folder, such as:

```text
C:\Users\your-username\AndroidStudioProjects
```

Do not clone the project into the WSL filesystem, a network drive, or a cloud-synchronized folder such as OneDrive.

## 4. Run the Starter App

Before changing any code:

1. Select an Android emulator or connected Android device.
2. Select the `app` run configuration.
3. Select **Run**.
4. Confirm that the starter app builds and displays the following placeholder information:

   ```text
   Welcome to CS 3170!
   
   Name: YOUR NAME
   ```

If the unchanged starter project does not build or run, stop and ask for assistance before continuing.

## 5. Add Your Name

1. Open `MainActivity.kt`.
2. Find the following line:

   ```kotlin
      Text(
         text = "Name: YOUR_NAME",
         fontSize = 32.sp
      )
   ```

3. Replace `YOUR NAME` with your full name. Keep the quotation marks.

4. Either run the app and confirm that your name appears on the screen, or verify your name shows up in the preview

### Commit the Change

1. Select **Git > Commit**.
2. Select `MainActivity.kt` in the list of changed files.
3. Review the displayed differences and make sure only your intended change is included.
4. Enter the following commit message:

   ```text
   Add my name to the welcome screen
   ```

5. Select **Commit**. Do not select **Commit and Push** yet.

If Android Studio asks for your Git author information, enter your name and an email address associated with your GitHub account. This information identifies the author of the commit; it is not your GitHub password.

## 6. Add Another Text Element

In the same composable, locate this comment:

   ```kotlin
           // TODO: Add a Text composable to display your major (i.e. BSCS, BACS, BSCEG, BSITC, etc...)
   ```

Add a third `Text` Composable that will display your major

- Add a new `Text` composable rather than changing an existing one.
- Remove the `TODO` comment
- The project should still build and run, or you should see the changes in the preview.

### Commit the Change

1. Select **Git > Commit**.
2. Select `MainActivity.kt`.
3. Review your changes.
4. Enter the following commit message:

   ```text
   Add major to the welcome screen
   ```

5. Select **Commit**.

You should now have two commits that have not yet been pushed to GitHub.

## 7. Push Your Commits

A commit records a change in the Git repository on your computer. A push sends your local commits to GitHub.

1. Select **Git > Push**.
2. Confirm that the Push dialog lists both of your commits.
3. Select **Push**.
4. Wait for Android Studio to report that the push completed successfully.

For this assignment, pushing your work to GitHub submits it for grading. You do not need to upload a ZIP file or submit the project separately in Pilot.

## 8. Verify Your Submission

Open your assignment repository on GitHub and confirm that:

- `MainActivity.kt` contains your completed changes.
- Your name is no longer displayed as `YOUR NAME`.
- The `TODO` comment has been removed.
- Your repository contains both required commits.
- The latest automated check completes successfully.

You can also return to Classroom 50 and open **My submission** to view the status of your work.

Your work is not submitted until your commits are visible on GitHub.

## Personal Access Token Troubleshooting

Use these steps only if **Log In via GitHub** does not work or your instructor directs you to use a token.

1. In Android Studio, open **Settings > Version Control > GitHub**.
2. Select **Add account > Log In with Token**.
3. Select **Generate**.
4. Sign in to GitHub if prompted.
5. Give the token a descriptive name, such as `Android Studio CS 3170`.
6. Set the token to expire near the end of the semester.
7. Generate the token and copy it.
8. Return to Android Studio, paste the token into the token field, and select **Add Account** or **Log In**.

A personal access token functions like a password:

- Never paste it into a source-code file, commit, README, discussion post, email, or screenshot.
- Do not use your normal GitHub password when Git asks for a password. GitHub does not accept account passwords for Git operations.
- If a token is accidentally exposed, revoke it immediately in your GitHub settings and create a new one.

If authentication still fails, verify that you are signed in with the correct GitHub account and that you accepted the course organization invitation.

## Grading Rubric - 10 Points

| Requirement                                                                                            | Points |
|--------------------------------------------------------------------------------------------------------|--------|
| The project builds successfully and the completed screen can be displayed                              | 2      |
| Your full name replaces `YOUR_NAME` in the existing `Text` composable                                  | 3      | 
| Your major is displayed using a new `Text` composable                                                  | 3      | 
| You have two commits with meaningful commit messages | 2      |


