Application permettant d'afficher les informations du compte Papercut (https://www.papercut.com) de l'utilisateur.

============================

https://www.esup-portail.org/wiki/display/EsupPapercut

Papercut est un outil payant de gestion d'impressions. Il propose notamment une gestion de quotas d'impressions.

En plus d'afficher le nombre d'impressions et le quota restant, esup-papercut peut permettre à l'utilisateur de recréditer son compteur Papercut, ce via un paiement Paybox ou Izlypay.

### Configurations

* src/main/resources/logback.xml pour les logs
* src/main/resources/esup-papercut.properties pour tout le reste

## Installation 

### Pré-requis
* Java Open JDK 8 : https://openjdk.java.net/install : le mieux est de l'installer via le système de paquets de votre linux.
* Maven (dernière version 3.0.x) : http://maven.apache.org/download.cgi : le mieux est de l'installer via le système de paquets de votre linux.
* Postgresql 9 : le mieux est de l'installer via le système de paquets de votre linux.
* Tomcat 9

### PostgreSQL
* pg_hba.conf : ajout de 

``` 
host    all             all             127.0.0.1/32            password
``` 

* redémarrage de postgresql
* psql

```
create database esuppapercut;
create USER esuppapercut with password 'esup';
grant ALL ON DATABASE esuppapercut to esuppapercut;
```

### Paramétrage mémoire JVM :

Pensez à paramétrer les espaces mémoire JVM : 
```
export JAVA_OPTS="-Xms256m -Xmx256m"
```

### Obtention du war pour déploiement sur tomcat ou autre :
```
mvn clean package
```


