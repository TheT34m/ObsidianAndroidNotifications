# ObsidianAndroidNotifications
This application provides a solution to receive native android os notification from your obisidan notes!

Example for a reminder:

`- [ ] my very important task (@2023-02-28 12:00)`

# How it works

This android app is observing the config file of Obsidian Reminder plugin which contains the reminders from your notes. Then it schedules android native alarms based on them.

## Usage

- Install Obsidian Reminder Plugin in your vault (https://github.com/uphy/obsidian-reminder)
- Install DriveSync to your phone (https://play.google.com/store/apps/details?id=com.ttxapps.drivesync)
- Configure drive sync:
  - enable autosync in global settings
  - sync method: two-way
  - uncheck 'exclude hidden files'
- Install ObisidianAndroidNotifications
- Configure ObisidianAndroidNotifications:
  - add your reminder plugin config file(YOUR_VAULT/.obisidan/plugins/obisidian-reminder-plugin/data.json)  as watched folder
- drink a beer! 🍺

### Tips
Please note that the foreground service is immutable, which means that the notification will remain visible even if you try to dismiss it.
But you can mute it, if you mute the `OBSIDIAN_TASK_FOREGROUND` category in the app's notification.
