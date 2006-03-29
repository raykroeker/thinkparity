@echo off

@rem ***************************************************************************
@rem Setup browser2 env
@rem ***************************************************************************
call setBrowser2Env.cmd

@rem ***************************************************************************
@rem Setup browser2 test env
@rem ***************************************************************************
set B2_OPTS=-Dparity.serverhost=rk-mobile.raykroeker.com -Dparity.workspace="%APPDATA%\Parity Software\Parity.Dev.%1" %B2_OPTS%
