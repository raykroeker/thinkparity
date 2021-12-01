;NSIS Modern User Interface
;Parity Installation Script
; raymond@raykroeker.com

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "thinkParity Archive"
  OutFile "thinkParity-${pom.version} Archive.exe"

  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\thinkParity" ""
;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "thinkParity" SecArchive

  SetOutPath "$INSTDIR"

  CreateDirectory "$INSTDIR\plugins"
  File /r "plugins"

  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall thinkParity Archive.exe"

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecArchive ${LANG_ENGLISH} "thinkParity Archive"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecArchive} $(DESC_SecArchive)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  Delete "$INSTDIR\plugins\thinkParity Archive.par"
  Delete "$INSTDIR\Uninstall thinkParity Archive.exe"

SectionEnd
