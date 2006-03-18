@echo off
setlocal

@rem ***************************************************************************
@rem Setup workspace
@rem ***************************************************************************
set B2_OPTS=-Dparity.workspace="%APPDATA%\Parity Software\Parity.dJohnson"

@rem ***************************************************************************
@rem Run the browser
@rem ***************************************************************************
call browser2.cmd

endlocal
