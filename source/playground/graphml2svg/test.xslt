 <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/2000/svg">
   <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
   <xsl:template match="graph">
     <!-- first give a CSS reference for the generated SVG -->
     <xsl:processing-instruction name="xml-stylesheet">type="text/css" href="default.css"</xsl:processing-instruction> 
     <svg>
       <defs>
         <marker id="arrow" refX="5" refY="5" markerUnits="userSpaceOnUse" markerWidth="10" markerHeight="10" orient="auto">
           <path class="edge" d="M0 0 10 5 0 10z"/>
         </marker>
       </defs>
       <xsl:for-each select="node">
         <!-- give to the 'g' the node class for styling -->
         <g class="node">
           <rect width="100" height="100"/>
           <text>
             <xsl:value-of select="@id"/>
           </text>
         </g>
       </xsl:for-each>
       <xsl:for-each select="edge">
         <!-- give to the 'line' the edge class for styling -->
         <line class="edge">
           <xsl:if test="not(@directed='false')">
             <!-- if the edge is directed add the directed CSS class -->
             <xsl:attribute name="class">egde directed </xsl:attribute>
           </xsl:if>
         </line>
       </xsl:for-each>
     </svg>
   </xsl:template>
 </xsl:stylesheet>
