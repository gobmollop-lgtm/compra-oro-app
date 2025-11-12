FROM tomcat:9.0-jdk11

# Eliminar la app por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar tu WAR como ROOT.war (se despliega en la raíz: /)
COPY dist/CompraOroApp.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8080 (estándar de Tomcat)
EXPOSE 8080

# Iniciar Tomcat en modo foreground
CMD ["catalina.sh", "run"]
