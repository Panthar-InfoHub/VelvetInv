import SwiftUI
import UIKit
import FirebaseCore
import FirebaseInstallations
import FirebaseMessaging
import UserNotifications
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()

        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound]
        ) { _, error in
            if let error {
                print("Notification permission request failed: \(error)")
            }
        }
        application.registerForRemoteNotifications()

        return true
    }

    /**
     * SwiftUI apps don't get Firebase's app delegate swizzling (FirebaseAppDelegateProxyEnabled is
     * off), so the APNs token is handed to FCM here. Without it FCM never registers the
     * installation ID, and sends fail with UNREGISTERED.
     */
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Swift.Error
    ) {
        print("APNs registration failed: \(error)")
    }

    /**
     * FCM registers this app instance by its Firebase Installation ID (FirebaseMessagingInstallationIdEnabled
     * in Info.plist). The backend receives the same ID at login, through FirebaseInstallationIdBridge.
     */
    func messaging(_ messaging: Messaging, didReceiveRegistration installationId: String?) {
        print("FCM registered with installation ID: \(installationId ?? "nil")")
    }

    /** Shows notifications that arrive while the app is open, instead of dropping them. */
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        PushNotificationHandler.shared.onPushReceived(
            txn: notification.request.content.userInfo["txn"] as? String
        )
        completionHandler([.banner, .sound, .badge])
    }

    /** A tap on a notification that arrived while the app was in the background. */
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        PushNotificationHandler.shared.onPushReceived(
            txn: response.notification.request.content.userInfo["txn"] as? String
        )
        completionHandler()
    }
}

/** Hands the Firebase Installation ID to the shared code, which sends it to the backend at login. */
final class FirebaseInstallationIdBridge: NSObject, FirebaseInstallationIdProvider {
    func fetchInstallationId(onResult: @escaping (String?) -> Void) {
        Installations.installations().installationID { id, error in
            if let error {
                print("Firebase installation ID fetch failed: \(error)")
            }
            onResult(id)
        }
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
