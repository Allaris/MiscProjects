This `xdotool` shortcut will switch to the Zoom window, use the Zoom shortcut (Ctrl+A) to toggle mute, and then switch back to the last active window. Pair this with Ubuntu system shortcuts to mute Zoom from any window instead of just Zoom's window.

```
#!/bin/bash
CURWINNAME=$(xdotool getwindowfocus getwindowname) && CURWINID="$(xdotool search --name "$CURWINNAME")" && xdotool search --desktop 0 --name "Zoom Meeting" windowactivate --sync && sleep 0.1 && xdotool key Alt+a && xdotool windowactivate $CURWINID
```

Save that command to a *.sh file and then in the Ubuntu System Settings for Keyboard Shortcuts set a key shortcut to run the shell script you just created. The sleep in the command is needed allow the Zoom window to become active before toggling mute because it doesn't happen instantly. Increase the sleep if it isn't working but the Zoom window is being switched to momentarily.
