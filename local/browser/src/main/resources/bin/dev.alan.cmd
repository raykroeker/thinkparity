@echo off
setlocal

@rem ***************************************************************************
@rem Setup browser2 environment
@rem ***************************************************************************
call setBrowser2DevEnv.cmd alan

set B2_ARGS=

java %B2_OPTS% -cp %B2_CP% %B2_MAIN% %B2_ARGS%

endlocal
