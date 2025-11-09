FROM tomcat:9.0-jdk11

# Eliminar app por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar tu app WAR como ROOT.war (se desplegará en la raíz)
COPY dist/CompraOroApp.war /usr/local/tommycat/webapps/ROOT.war

# Exponer puerto
EXPOSE 8080

# Iniciar Tomcat
CMD ["catalina.sh", "run"]
