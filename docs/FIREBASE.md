# Firebase setup and security

Gezdir uses Firebase Authentication, Realtime Database, Cloud Firestore, Cloud Storage, and Cloud Messaging. The checked-in rules deny access by default and grant only the minimum access used by the Android client.

## Local configuration

1. Create or select a Firebase project.
2. Register an Android app with package name `com.example.gezdir`.
3. Download `google-services.json` from Firebase Console.
4. Place it at `app/google-services.json`. This file is intentionally ignored by Git.
5. Enable Email/Password authentication.
6. Create Realtime Database, Firestore, and Storage instances.
7. Upload a default profile image to `images/default.png`.

`app/google-services.example.json` documents the expected shape but is not a working credential file. Firebase client API keys are not server secrets, but a public repository should still use a dedicated Firebase project, API-key restrictions, App Check, quotas, and restrictive Security Rules.

## Deploy rules and indexes

Install the Firebase CLI, authenticate, select the correct project, and review the target before deployment:

```bash
firebase use --add
firebase deploy --only database,firestore:rules,firestore:indexes,storage
```

Test changes against the Firebase Emulator Suite before deploying to production:

```bash
firebase emulators:start
```

## Data model assumptions

- Realtime Database user profiles are stored at `users/{authUid}`.
- Adverts contain an immutable `ownerId` equal to the creator's Auth UID.
- User-uploaded images are stored under `images/{authUid}/...`.
- Firestore conversations contain `userOne` and `userTwo` Auth UIDs.
- Messages and notifications reference the conversation and use Auth UIDs for sender and receiver IDs.
- Each FCM token is stored as `fcmTokens/{authUid}` and is writable only by that user.

Authenticated users can currently read other profiles because username lookup is implemented client-side. A production evolution should split private account data from public profiles and enforce username uniqueness on a trusted backend.

## Migration from the legacy schema

The original app stored profiles under generated Realtime Database keys and included plaintext passwords. Do not deploy the new rules over that dataset without migrating it:

1. Back up Realtime Database and Storage.
2. For each Firebase Auth user, move the profile to `users/{authUid}`.
3. Remove every `password` field. Firebase Authentication is the only password store.
4. Add `ownerId` to every advert and set it to the owner's Auth UID.
5. Move user images to `images/{authUid}/...` and update stored paths.
6. Rewrite conversation, message, notification, and token user references to Auth UIDs.
7. Validate the app against the Emulator Suite, then deploy the rules.

## Push notifications

The legacy client embedded an FCM server key and called the deprecated legacy FCM endpoint. That path has been removed. Sending push notifications must happen in a trusted environment such as Cloud Functions using the Firebase Admin SDK. The Android app only registers its own token and receives messages.

Before production, enable App Check (Play Integrity), restrict the Android API key to the package name and signing certificate, configure budget alerts, and rotate any server key that was previously exposed.
