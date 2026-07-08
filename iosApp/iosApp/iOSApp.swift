import SwiftUI
import UIKit
import FirebaseCore
import FirebaseMessaging
import UserNotifications

class AppDelegate: NSObject,
                   UIApplicationDelegate,
                   MessagingDelegate,
                   UNUserNotificationCenterDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {

        print("========================================")
        print("🚀 APP LAUNCHED")
        print("========================================")

        print("Bundle Identifier:")
        print(Bundle.main.bundleIdentifier ?? "nil")

        print("Firebase plist exists:")
        print(Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") ?? "NOT FOUND")

        FirebaseApp.configure()

        print("Firebase configured:")
        print(FirebaseApp.app() != nil)

        if let options = FirebaseApp.app()?.options {
            print("Google App ID:")
            print(options.googleAppID)

            print("Project ID:")
            print(options.projectID)

            print("GCM Sender ID:")
            print(options.gcmSenderID)
        }

        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        print("Current notification settings...")

        UNUserNotificationCenter.current().getNotificationSettings { settings in
            print("-----------------------------")
            print("Authorization Status:")
            print(settings.authorizationStatus.rawValue)
            print("Alert:")
            print(settings.alertSetting.rawValue)
            print("Badge:")
            print(settings.badgeSetting.rawValue)
            print("Sound:")
            print(settings.soundSetting.rawValue)
            print("-----------------------------")
        }

        print("Requesting notification permission...")

        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound]
        ) { granted, error in

            DispatchQueue.main.async {

                print("Permission callback invoked")

                if let error = error {
                    print("Permission error:")
                    print(error)
                    return
                }

                print("Permission granted:")
                print(granted)

                if granted {
                    print("Calling registerForRemoteNotifications()")
                    application.registerForRemoteNotifications()
                    print("registerForRemoteNotifications() returned")
                } else {
                    print("Permission denied")
                }
            }
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + 5) {

            print("----- Delayed FCM request -----")

            Messaging.messaging().token { token, error in

                if let error = error {
                    print(error)
                }

                print("Delayed token:")
                print(token ?? "nil")
            }
        }

        return true
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        print("applicationDidBecomeActive")
    }

    func applicationWillResignActive(_ application: UIApplication) {
        print("applicationWillResignActive")
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {

        print("===================================")
        print("APNS REGISTRATION SUCCESS")
        print("===================================")

        let token = deviceToken.map {
            String(format: "%02x", $0)
        }.joined()

        print("APNS TOKEN:")
        print(token)

        Messaging.messaging().apnsToken = deviceToken

        print("Assigned APNS token to Firebase")

        Messaging.messaging().token { token, error in

            print("FCM completion invoked")

            if let error = error {
                print("FCM ERROR:")
                print(error)
            }

            print("FCM TOKEN:")
            print(token ?? "nil")
        }
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {

        print("===================================")
        print("APNS REGISTRATION FAILED")
        print("===================================")
        print(error)
    }

    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {

        print("===================================")
        print("MESSAGING DELEGATE")
        print("===================================")
        print("Token:")
        print(fcmToken ?? "nil")
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {

        print("📩 Foreground notification received")
        print(notification.request.content.userInfo)

        completionHandler([.banner, .sound, .badge])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {

        print("👆 Notification tapped")
        print(response.notification.request.content.userInfo)

        completionHandler()
    }

}

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}