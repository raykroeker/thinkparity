; thinkParity Modern User Interface NSIS Installer
; raykroeker@gmail.com

  ; mui configuration
  !define MUI_ABORTWARNING
  !define MUI_ICON "InstallIcon.ico"
  !define MUI_UNICON "InstallIcon.ico"

  ; mui include
  !include "MUI.nsh"

  ; global configuration
  name "thinkParity"
  outfile "InstallThinkParity.exe"
  installdir "$PROGRAMFILES\thinkParity"
  viproductversion "1.0.0.0"
  viaddversionkey "ProductName" "thinkParity ™"
  viaddversionkey "CompanyName" "thinkParity Solutions Inc."
  viaddversionkey "LegalCopyright" "© thinkParity Solutions Inc."
  viaddversionkey "FileDescription" "thinkParity Installation Binary"
  viaddversionkey "FileVersion" "${thinkparity.release-name}"
  viaddversionkey "Build" "${thinkparity.release-name}"
  autoclosewindow true

  ; global variables
  var STARTMENU_FOLDER
  var TMP

  ; page configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\thinkParity" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"

  ; installer pages
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_STARTMENU Application $STARTMENU_FOLDER
  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE InvokeThinkParity
  !insertmacro MUI_PAGE_INSTFILES

  ; uninstaller pages
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

  ; language
  !insertmacro MUI_LANGUAGE "English"

; install
section "thinkParity" SecParityBrowser
  setoutpath "$INSTDIR"

  file /r "${thinkparity.release-name}"
  file /r "jre1.6.0_01"
  file "LICENSE.TXT"
  file "README.TXT"
  file "thinkParity.exe"
  file "thinkParity.jar"
  file "thinkParity.properties"

  ; uninstaller
  writeuninstaller "$INSTDIR\Uninstall thinkParity.exe"

  ; shortcuts
  createdirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
  createshortcut "$SMPROGRAMS\$STARTMENU_FOLDER\thinkParity.lnk" "$INSTDIR\thinkParity.exe"
  createshortcut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall thinkParity.lnk" "$INSTDIR\Uninstall thinkParity.exe"
  createshortcut "$DESKTOP\thinkParity.lnk" "$INSTDIR\thinkParity.exe"
  createshortcut "$SMSTARTUP\thinkParity.lnk" "$INSTDIR\thinkParity.exe"

  ; save start menu folder
  writeregstr HKCU "Software\thinkParity\${thinkparity.release-name}" "StartMenuFolder" $STARTMENU_FOLDER
sectionend

; uninstall
section "Uninstall"

  ; load start menu folder
  readregstr $STARTMENU_FOLDER HKCU "Software\thinkParity\${thinkparity.release-name}" "StartMenuFolder"

  ; clean registry
  deleteregkey HKCU "Software\thinkParity\${thinkparity.release-name}"
  deleteregkey /ifempty HKCU "Software\thinkParity"

  ; this works because the uninstaller is in the install directory
  rmdir /r "$INSTDIR"

  ; delete start menu
  delete "$SMSTARTUP\thinkParity.lnk"
  delete "$DESKTOP\thinkParity.lnk"
  delete "$SMPROGRAMS\$STARTMENU_FOLDER\thinkParity.lnk"
  delete "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall thinkParity.lnk"
  ; recursively delete up to program files
  strcpy $TMP "$SMPROGRAMS\$STARTMENU_FOLDER"
uninstall_start_begin:
    rmdir $TMP
    getfullpathname $TMP "$TMP\.."

    ; if we hit start
    strcmp $TMP $SMPROGRAMS uninstall_start_end uninstall_start_begin
uninstall_start_end:
sectionend

function InvokeThinkParity
    exec "$INSTDIR\thinkParity.exe"
functionend
