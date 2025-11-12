FROM tomcat:9.0-jdk11

# Eliminar app por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar tu app WAR como ROOT.war (se desplegará en la raíz)
COPY dist/CompraOroApp.war /usr/local/tomcat/webapps/ROOT.war

# Exponer puerto (por defecto 8080, Railway lo puede cambiar)
EXPOSE 8080

# Tomcat usará el puerto que defina la variable PORT (Railway lo inyecta)
ENV PORT 8080
RUN sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml

# Iniciar Tomcat
CMD ["catalina.sh", "run"]
