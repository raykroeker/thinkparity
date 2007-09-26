;NSIS Modern User Interface
;Parity Installation Script
;raykroeker@gmail.com

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "thinkParity"
  OutFile "InstallThinkParity.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\thinkParity"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\thinkParity" ""
;--------------------------------
;Variables

  Var MUI_TEMP
  Var STARTMENU_FOLDER
;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY

  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\thinkParity" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"

  !insertmacro MUI_PAGE_STARTMENU Application $STARTMENU_FOLDER

  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;File Properties
VIProductVersion "1.0.0.0"
VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "thinkParity ™"
VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "thinkParity Solutions Inc."
VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "© thinkParity Solutions Inc."
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "thinkParity Binary"
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "${thinkparity.release-name}"

;--------------------------------
;Installer Sections

Section "thinkParity" SecParityBrowser

  SetOutPath "$INSTDIR"

  File /r "${thinkparity.release-name}"
  File /r "jre1.6.0_01"
  File "LICENSE.TXT"
  File "README.TXT"
  File "thinkParity.exe"
  File "thinkParity.jar"
  File "thinkParity.properties"

  ;Store installation folder
  WriteRegStr HKCU "Software\thinkParity" "" $INSTDIR

  ;Create shortcuts
  CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
  CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
  CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\thinkParity.lnk" "$INSTDIR\thinkParity.exe"
  CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall thinkParity.lnk" "$INSTDIR\Uninstall thinkParity.exe"
  CreateShortCut "$DESKTOP\thinkParity.lnk" "$INSTDIR\thinkParity.exe"
  CreateShortCut "$SMSTARTUP\thinkParity.lnk" "$INSTDIR\thinkParity.exe"

  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall thinkParity.exe"

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecParityBrowser ${LANG_ENGLISH} "thinkParity"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecParityBrowser} $(DESC_SecParityBrowser)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  RMDir /r "$INSTDIR"

  ; extract the location of the start menu
  !insertmacro MUI_STARTMENU_GETFOLDER Application $MUI_TEMP

  Delete "$SMPROGRAMS\$MUI_TEMP\thinkParity.lnk"
  Delete "$SMPROGRAMS\$MUI_TEMP\Uninstall thinkParity.lnk"
  Delete "$DESKTOP\thinkParity.lnk"
  Delete "$SMSTARTUP\thinkParity.lnk"

  ; Delete empty start menu parent diretories
  StrCpy $MUI_TEMP "$SMPROGRAMS\$MUI_TEMP"

  startMenuDeleteLoop:
    ClearErrors
    RMDir $MUI_TEMP
    GetFullPathName $MUI_TEMP "$MUI_TEMP\.."

    IfErrors startMenuDeleteLoopDone

    StrCmp $MUI_TEMP $SMPROGRAMS startMenuDeleteLoopDone startMenuDeleteLoop
  startMenuDeleteLoopDone:

  Delete "$INSTDIR\Uninstall thinkParity.exe"

  RMDir /r "$INSTDIR"
  RMDIR "$PROGRAMFILES\thinkParity"

  DeleteRegKey /ifempty HKCU "Software\thinkParity"

SectionEnd