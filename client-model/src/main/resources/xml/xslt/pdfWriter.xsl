<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
  <xsl:variable name="fontFamily">Times</xsl:variable>
  <xsl:variable name="bodyTextSize">12pt</xsl:variable>
  <xsl:variable name="tableSpaceAfter">0.2cm</xsl:variable>
  <xsl:variable name="tableWidthColumn1">4cm</xsl:variable>
  <xsl:variable name="tableWidthColumn2">12cm</xsl:variable>

  <!-- resources -->
  <xsl:template match="resources">
  </xsl:template>

  <!-- resource name -->
  <xsl:template match="name">
    <xsl:if test=". = 'header-image'">
      <xsl:variable name="header-image"><xsl:value-of select="../path"/></xsl:variable>
      <fo:static-content flow-name="xsl-region-before">
        <fo:external-graphic src="{$header-image}"/>
      </fo:static-content>
    </xsl:if>
    <xsl:if test=". = 'footer-image'">
      <xsl:variable name="footer-image"><xsl:value-of select="../path"/></xsl:variable>
      <fo:static-content flow-name="xsl-region-after">
        <!-- fo:block font-size="{$bodyTextSize}" text-align="end" margin-right="1cm">Page <fo:page-number/> of <fo:page-number-citation ref-id="terminator"/></fo:block> -->
        <fo:external-graphic src="{$footer-image}"/>
      </fo:static-content>
    </xsl:if>
  </xsl:template>

  <!-- container -->
  <xsl:template match="container">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="0cm" margin-bottom="0cm" margin-left="0cm" margin-right="0cm">
          <fo:region-body margin-top="4cm" margin-bottom="2cm" margin-left="2cm" margin-right="3cm"/>
          <fo:region-before extent="4cm"/>
          <fo:region-after extent="1cm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <xsl:apply-templates select="../resources/resource/name"/>
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="30pt" font-family="{$fontFamily}" space-after="3mm"><xsl:value-of select="name"/></fo:block>
          <fo:block font-size="{$bodyTextSize}" font-family="{$fontFamily}" margin-left="1cm">
            <xsl:call-template name="displayValue">
              <xsl:with-param name="itemLeft"><xsl:value-of select="localization/firstPublished"/>:</xsl:with-param>
              <xsl:with-param name="itemRight"><xsl:value-of select="firstPublished"/></xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="displayValue">
              <xsl:with-param name="itemLeft"><xsl:value-of select="localization/lastPublished"/>:</xsl:with-param>
              <xsl:with-param name="itemRight"><xsl:value-of select="lastPublished"/></xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="displayTeamMembers"/>
            <xsl:call-template name="displayDocumentSummaries"/>
            <xsl:call-template name="displayVersionSummaries"/>
          </fo:block>
          <xsl:apply-templates select="versions/version">
            <xsl:with-param name="versionPublishedOn"><xsl:value-of select="localization/versionPublishedOn"/></xsl:with-param>
            <xsl:with-param name="versionPublishedBy"><xsl:value-of select="localization/versionPublishedBy"/></xsl:with-param>
            <xsl:with-param name="versionPublishedTo"><xsl:value-of select="localization/versionPublishedTo"/></xsl:with-param>
            <xsl:with-param name="versionDocuments"><xsl:value-of select="localization/versionDocuments"/></xsl:with-param>
            <xsl:with-param name="versionNote"><xsl:value-of select="localization/versionNote"/></xsl:with-param>
          </xsl:apply-templates>
          <fo:block id="terminator"/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>

  <!-- version -->
  <xsl:template match="version">
    <xsl:param name="versionPublishedOn"></xsl:param>
    <xsl:param name="versionPublishedBy"></xsl:param>
    <xsl:param name="versionPublishedTo"></xsl:param>
    <xsl:param name="versionDocuments"></xsl:param>
    <xsl:param name="versionNote"></xsl:param>
    <fo:block space-before="5mm" border-top-style="solid" border-top-color="rgb(0,0,0)" border-top-width="thin" padding-top="2mm" margin-left="1cm"/>
    <fo:block space-before="3mm" font-size="14pt" font-family="{$fontFamily}" font-weight="bold" margin-left="1cm" space-after="3mm">
      <fo:block><xsl:value-of select="name"/></fo:block>
    </fo:block>
    <fo:block space-before="3mm" font-size="{$bodyTextSize}" font-family="{$fontFamily}" margin-left="1cm">
      <xsl:call-template name="displayValue">
        <xsl:with-param name="itemLeft"><xsl:value-of select="$versionPublishedOn"/>:</xsl:with-param>
        <xsl:with-param name="itemRight"><xsl:value-of select="publishedOn"/></xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="displayValue">
        <xsl:with-param name="itemLeft"><xsl:value-of select="$versionPublishedBy"/>:</xsl:with-param>
        <xsl:with-param name="itemRight"><xsl:value-of select="publishedBy"/></xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="displayUsers">
        <xsl:with-param name="heading"><xsl:value-of select="$versionPublishedTo"/></xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="displayDocuments">
        <xsl:with-param name="heading"><xsl:value-of select="$versionDocuments"/></xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="displayValue">
        <xsl:with-param name="itemLeft"><xsl:value-of select="$versionNote"/>:</xsl:with-param>
        <xsl:with-param name="itemRight"><xsl:value-of select="note"/></xsl:with-param>
      </xsl:call-template>
    </fo:block>
  </xsl:template>

  <!-- displayDocuments -->
  <xsl:template name="displayDocuments">
    <xsl:param name="heading"></xsl:param>
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <xsl:apply-templates select="documents/document">
          <xsl:with-param name="heading"><xsl:value-of select="$heading"/></xsl:with-param>
        </xsl:apply-templates>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- displayDocumentSummaries -->
  <xsl:template name="displayDocumentSummaries">
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <xsl:apply-templates select="documentSummaries/documentSummary">
          <xsl:with-param name="heading"><xsl:value-of select="localization/documents"/></xsl:with-param>
          <xsl:with-param name="documentSum"><xsl:value-of select="documentSum"/></xsl:with-param>
        </xsl:apply-templates>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- displayTeamMembers -->
  <xsl:template name="displayTeamMembers">
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <xsl:apply-templates select="teamMembers/teamMember">
          <xsl:with-param name="heading"><xsl:value-of select="localization/teamMembers"/></xsl:with-param>
          <xsl:with-param name="teamMemberSum"><xsl:value-of select="teamMemberSum"/></xsl:with-param>
        </xsl:apply-templates>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- displayUsers -->
  <xsl:template name="displayUsers">
    <xsl:param name="heading"></xsl:param>
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <xsl:apply-templates select="users/user">
          <xsl:with-param name="heading"><xsl:value-of select="$heading"/></xsl:with-param>
        </xsl:apply-templates>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- displayValue -->
  <xsl:template name="displayValue">
    <xsl:param name="itemLeft"></xsl:param>
    <xsl:param name="itemRight"></xsl:param>
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell>
            <fo:block font-weight="bold"><xsl:value-of select="$itemLeft"/></fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block><xsl:value-of select="$itemRight"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- displayVersionSummaries -->
  <xsl:template name="displayVersionSummaries">
    <fo:table table-layout="fixed" space-after="{$tableSpaceAfter}">
      <fo:table-column column-width="{$tableWidthColumn1}"/>
      <fo:table-column column-width="{$tableWidthColumn2}"/>
      <fo:table-body>
        <xsl:apply-templates select="versionSummaries/versionSummary">
          <xsl:with-param name="heading"><xsl:value-of select="localization/versions"/></xsl:with-param>
          <xsl:with-param name="versionSum"><xsl:value-of select="versionSum"/></xsl:with-param>
        </xsl:apply-templates>
      </fo:table-body>
    </fo:table>
  </xsl:template>

  <!-- document -->
  <xsl:template match="document[position() = 1]">
    <xsl:param name="heading"></xsl:param>
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-weight="bold"><xsl:value-of select="$heading"/>:</fo:block>
      </fo:table-cell>
      <xsl:call-template name="documentPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="document">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <xsl:call-template name="documentPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="documentPrint">
    <fo:table-cell>
      <fo:block><xsl:value-of select="name"/><xsl:value-of select="delta"/></fo:block>
    </fo:table-cell>
  </xsl:template>

  <!-- documentSummary -->
  <xsl:template match="documentSummary[position() = 1]">
    <xsl:param name="heading"></xsl:param>
    <xsl:param name="documentSum"></xsl:param>
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-weight="bold"><xsl:value-of select="$heading"/> (<xsl:value-of select="$documentSum"/>):</fo:block>
      </fo:table-cell>
      <xsl:call-template name="documentVersionPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="documentSummary">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <xsl:call-template name="documentVersionPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="documentVersionPrint">
    <fo:table-cell>
      <fo:block><xsl:value-of select="name"/></fo:block>
    </fo:table-cell>
  </xsl:template>

  <!-- teamMember -->
  <xsl:template match="teamMember[position() = 1]">
    <xsl:param name="heading"></xsl:param>
    <xsl:param name="teamMemberSum"></xsl:param>
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-weight="bold"><xsl:value-of select="$heading"/> (<xsl:value-of select="$teamMemberSum"/>):</fo:block>
      </fo:table-cell>
      <xsl:call-template name="teamMemberPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="teamMember">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <xsl:call-template name="teamMemberPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="teamMemberPrint">
    <fo:table-cell>
      <fo:block><xsl:value-of select="name"/> (<xsl:value-of select="title"/>, <xsl:value-of select="organization"/>)</fo:block>
    </fo:table-cell>
  </xsl:template>

  <!-- user -->
  <xsl:template match="user[position() = 1]">
    <xsl:param name="heading"></xsl:param>
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-weight="bold"><xsl:value-of select="$heading"/>:</fo:block>
      </fo:table-cell>
      <xsl:call-template name="userPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="user">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <xsl:call-template name="userPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="userPrint">
    <fo:table-cell>
      <fo:block><xsl:value-of select="name"/><xsl:value-of select="receivedOn"/></fo:block>
    </fo:table-cell>
  </xsl:template>

  <!-- versionSummary -->
  <xsl:template match="versionSummary[position() = 1]">
    <xsl:param name="heading"></xsl:param>
    <xsl:param name="versionSum"></xsl:param>
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-weight="bold"><xsl:value-of select="$heading"/> (<xsl:value-of select="$versionSum"/>):</fo:block>
      </fo:table-cell>
      <xsl:call-template name="versionSummaryPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="versionSummary">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <xsl:call-template name="versionSummaryPrint"/>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="versionSummaryPrint">
    <fo:table-cell>
      <fo:block><xsl:value-of select="name"/></fo:block>
    </fo:table-cell>
  </xsl:template>
</xsl:stylesheet>
