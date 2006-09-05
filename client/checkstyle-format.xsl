<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
      <html>
        <head>
        <title>thinkParity Style Violations</title>
        </head>
        <body>
            <p><b>thinkParity Code Style Results</b></p>
            <table border="1" cellspacing="0" cellpadding="2" width="100%">
                <tr>
                    <th colspan="2"><b>Summary</b></th>
                </tr>
                <tr>
                    <td width="20%">Total files checked</td>
                    <td><xsl:number level="any" value="count(descendant::file)"/></td>
                </tr>
                <tr>
                    <td>Files with errors</td>
                    <td><xsl:number level="any" value="count(descendant::file[error])"/></td>
                </tr>
                <tr>
                    <td>Total errors</td>
                    <td><xsl:number level="any" value="count(descendant::error)"/></td>
                </tr>
            </table>
            <hr align="center" width="95%" size="1"/>
            <table border="1" cellspacing="0" cellpadding="2" width="100%">
                <tr>
                    <th>Line Number</th>
                    <th>Error Message</th>
                </tr>
                <xsl:apply-templates/>
            </table>
        </body>
      </html>
    </xsl:template>

    <xsl:template match="file[error]">
        <tr>
            <td colspan="2"><xsl:value-of select="@name"/></td>
        </tr>
        <xsl:apply-templates select="error"/>
    </xsl:template>

    <xsl:template match="error">
        <tr>
            <td><xsl:value-of select="@line"/></td>
            <td><xsl:value-of select="@message"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
