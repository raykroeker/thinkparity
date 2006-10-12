<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
  <!-- container -->
  <xsl:template match="container">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="16pt" font-weight="bold" space-after="3mm">Package:  <xsl:value-of select="name"/></fo:block>
          <fo:block font-size="12pt">
            <fo:table table-layout="fixed">
              <fo:table-column column-width="3.5cm"/>
              <fo:table-column column-width="17cm"/>
              <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>Created By:</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><xsl:value-of select="createdBy"/></fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>Created On:</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><xsl:value-of select="createdOn"/></fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block> </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><xsl:value-of select="versionSum"/> versions.</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block> </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><xsl:value-of select="documentSum"/> documents.</fo:block>
                    </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <xsl:apply-templates/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <!-- version -->
  <xsl:template match="version">
    <fo:block space-before="3mm" border-top="solid black 1px" padding-top="2mm" font-size="12pt">
      <fo:table table-layout="fixed">
        <fo:table-column column-width="3.5cm"/>
        <fo:table-column column-width="17cm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>Published By:</fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block><xsl:value-of select="publishedBy"/></fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>Published On:</fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block><xsl:value-of select="publishedOn"/></fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row/>
          <fo:table-row>
            <fo:table-cell>
              <fo:block> </fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block><xsl:value-of select="documentSum"/> documents.</fo:block>
            </fo:table-cell>
          </fo:table-row>
          <xsl:apply-templates select="document"/>
          <fo:table-row/>
          <fo:table-row>
            <fo:table-cell>
              <fo:block> </fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block><xsl:value-of select="userSum"/> users.</fo:block>
            </fo:table-cell>
          </fo:table-row>
          <xsl:apply-templates select="user"/>
        </fo:table-body>
      </fo:table>
    </fo:block>
  </xsl:template>
  <!-- document -->
  <xsl:template match="document">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block><xsl:value-of select="name"/> - <xsl:value-of select="size"/></fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  <!-- user -->
  <xsl:template match="user">
    <fo:table-row>
      <fo:table-cell>
        <fo:block> </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block><xsl:value-of select="name"/> - <xsl:value-of select="receivedOn"/></fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
</xsl:stylesheet>
