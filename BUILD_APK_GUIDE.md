# Expense Tracker - GitHub Actions Build Guide

## 📱 Automated APK Builds with GitHub Actions

This project includes a fully configured GitHub Actions workflow that automatically builds the APK whenever you push code to the `main` or `master` branch.

## 🚀 How to Get Your APK

### Option 1: Automatic Build via GitHub Actions (Recommended)

1. **Push your code to GitHub:**
   ```bash
   git add .
   git commit -m "Update expense tracker features"
   git push origin main
   ```

2. **Go to your repository on GitHub**

3. **Navigate to Actions tab:**
   - Click on the "Actions" tab at the top of your repository
   - You'll see the "Build APK" workflow running

4. **Download the APK:**
   - Wait for the build to complete (green checkmark)
   - Click on the workflow run
   - Scroll down to the "Artifacts" section
   - Click on "ExpenseTracker-Debug-APK" to download
   - The APK will be named `ExpenseTracker-v1.0-debug.apk`

### Option 2: Manual Trigger

You can also manually trigger a build without pushing code:

1. Go to **Actions** → **Build APK**
2. Click **"Run workflow"** button
3. Select the branch
4. Click **"Run workflow"**
5. Download the APK once complete

## 📋 What the Workflow Does

The GitHub Actions workflow (`/.github/workflows/android.yml`) performs these steps:

1. ✅ Checks out your code
2. ☕ Sets up JDK 17 (Temurin distribution)
3. 🔧 Caches Gradle dependencies for faster builds
4. ✔️ Validates Gradle wrapper integrity
5. 🏗️ Builds the debug APK
6. ✅ Verifies APK was created successfully
7. 📦 Creates SHA256 checksum for security
8. ⬆️ Uploads APK as an artifact (retained for 30 days)

## 🔧 Local Build Alternative

If you prefer to build locally:

```bash
# Navigate to project directory
cd /workspace

# Make gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Find your APK
ls -la app/build/outputs/apk/debug/app-debug.apk
```

The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

## 📊 Build Artifacts

After a successful GitHub Actions build, you'll get:

| Artifact Name | Description | Retention |
|--------------|-------------|-----------|
| `ExpenseTracker-Debug-APK` | The installable APK file | 30 days |
| `Checksums` | SHA256 hash for verification | 30 days |

## 🔐 Security Notes

- The debug APK is signed with the default debug keystore
- For production releases, you should configure signing with a release keystore
- Never commit your keystore files or passwords to the repository
- Use GitHub Secrets for sensitive information in CI/CD

## 🛠️ Troubleshooting

### Build Fails?

Check the following:
1. All Kotlin files are properly formatted
2. No missing resources in `res/` folder
3. Dependencies in `build.gradle` are correct
4. Check the full logs in GitHub Actions for specific errors

### Can't Download Artifact?

- Artifacts expire after 30 days
- You need to be logged into GitHub
- Repository must be public or you need access permissions

### Want Release APK?

Modify the workflow to include:
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease
```

Note: Release builds require proper signing configuration.

## 📱 Installing the APK

1. Download the APK from GitHub Actions artifacts
2. Transfer to your Android device
3. Enable "Install from Unknown Sources" in Settings
4. Open the APK file and install
5. Launch Expense Tracker!

---

**Need help?** Check the [GitHub Actions Documentation](https://docs.github.com/en/actions) or open an issue in this repository.
