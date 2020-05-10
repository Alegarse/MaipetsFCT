# Maipets
Gestión de mascotas integral a nivel usuario.<br/>


## Video explicativo del contenido
[Video explicativo](https://youtu.be/JNNqqDwe2vU)


# Evolución semanal del proyecto

##  :books:Semana 23 a 27 de Marzo de 2020
### Contenido
En esta semana he estado retocando el proyecto base sobre el que voy a rediseñar este proyecto. Al contener bastantes lineas de código, algunas no optimizadas, decidí finalmente la opción de rehacerlo por completo, así que parto de uno nuevo que iré implementando en las semanas sucesivas.<br/>
He dedicado el tiempo que tenía en buscar información acerca de la posibilidad del uso de plataformas de redes sociales para el login en la aplicación, y dado que muchos videos y enlaces están desactualizados, ha sido en gran parte una pérdida de tiempo; también tratando de editar estetícamente a través de paginas que informan sobre las mejores combinaciones de colores para Material Design.<br/>
A nivel de código aún no he implementado nada propiamente dicho a través de Android Studio, lo comenzaré la próxima semana.<br/>
Tambíen he estado pensando como realizar la web de gestión, algo que aún está sin determinar.<br/>
### Bibliografía Utilizada
:small_orange_diamond:Buscando combinación de colores: [Link web con selección de colores](https://material.io/resources/color/#!/?view.left=1&view.right=0&primary.color=D32F2F&secondary.color=7986cc)<br/>
:small_orange_diamond:Buscando templates y diseños amigables: [Material Design Kit](https://materialdesignkit.com/templates/)<br/>
:small_orange_diamond:Eligiendo tipografía TrueType: [Google Fonts](https://fonts.google.com/)<br/>
:small_orange_diamond:Buscando información para integrar logins:<br/>
> :small_blue_diamond:[Login con google en Android](https://jonathanmelgoza.com/blog/como-hacer-un-login-con-google-en-android/)<br/>
> :small_blue_diamond:[Guía para desarrolladores Android](https://developer.android.com/guide)<br/>

##  :books:Semana 30 de Marzo al 3 de Abril de 2020
### Contenido
A la hora del código,he distribuido el inicio de la aplicación tenieno en cuenta los posible tipos de usuarios que podran hacer uso del contenido de la app, y conforme a ello he ido realizando dichas actividades, teniendo una interrupción debido a un fallo de ejecución en el emulador, crasheando la app, sin que haya podido aún solventarlo.<br/>
### Bibliografía Utilizada
:small_orange_diamond:[Login con google en Android](https://jonathanmelgoza.com/blog/como-hacer-un-login-con-google-en-android/)<br/>
:small_orange_diamond:[Normativa y uso de componentes Material Design](https://material.io/develop/android/)<br/>

##  :books:Semana 6 de Abril al 10 de Abril de 2020
### Contenido
A nivel de código no he avanzado debido a que persiste el error y no he sabido solventarlo; sí he seguido trabajando acerca de como modelar la interacción del usuario con la app y como realizarla acorde a la normativa de google, todo en forma de lectura comprensiva, esta semana ha sido complicada.<br/>
### Bibliografía Utilizada
:small_orange_diamond:[Guía arquitectura apps](https://developer.android.com/jetpack/docs/guide?hl=es-419)<br/>
:small_orange_diamond:[Política de desarrolladores](https://play.google.com/intl/es/about/developer-content-policy/)<br/>

##  :books:Semana 13 de Abril al 17 de Abril de 2020
### Contenido
Tras una consulta y aclaración al profesorado, finalmente consigo solventar la parte que hacía que fallase la app y puedo continuar con su desarrollo. Realizadas las pantallas de login y registro para cada uno de los usuarios.
### Bibliografía Utilizada
:small_orange_diamond:[Login con google en Android](https://jonathanmelgoza.com/blog/como-hacer-un-login-con-google-en-android/)<br/>
:small_orange_diamond:[Normativa y uso de componentes Material Design](https://material.io/develop/android/)<br/>

##  :books:Semana 20 de Abril al 24 de Abril de 2020
### Contenido
He editado la base de datos para tratar de usar la misma que ladela versión anterior, y estoy consultando como poder realizar la web de administración para ir teniendo una idea de como diseñarla, dado que aún no lo tengo muy claro. Si sé que se realizara su linkeo desde la app cuando detecte que quien la esté usando sea administrador.<br/>
Para la próxima semana si me meteré de lleno en darle mas forma al código de la app.<br/>
### Bibliografía Utilizada
:small_orange_diamond:[Legislación para páginas web](https://webysocialmedia.es/legislacion-aplicable-las-paginas-web-espanolas/)<br/>
:small_orange_diamond:[Busqueda de ideas](https://amalialopezacera.com/pagina-web-administracion-publica/)<br/>

##  :books:Semana 27 de Abril al 1 de Mayo de 2020
### Contenido
He seguido desarrollando los registros para que para cada usuario la aplicacion sepa distinguir la diferencia de perfil. Ello queda reflejado en la base de datos atendienda a uncódigo que incorpora cada usuario para posteriormente poder distinguirlos.<br/>
Comienzo con el desarrollo de la pagina principal de la aplicación a través del uso de fragmentos.<br/>
### Bibliografía Utilizada
:small_orange_diamond:[Documentación fragmentos](https://developer.android.com/guide/components/fragments?hl=es-419)<br/>
:small_orange_diamond:[Cómo comunicar contenido con otros fragmentos](https://developer.android.com/training/basics/fragments/communicating?hl=es)<br/>

##  :books:Semana 4 de Mayo al 8 de Mayo de 2020
### Contenido
He compuesto la actividad principal en tres fragmentos diferentes, que acaparan el perfil de usuario, la pagina principal de mascotas y el tercero, de servicios disponiles para las mascotas.<br/>
Realizada correctamente la opción paralogeo a través de Firebase.<br/>
Tras codificarlos y dejarlos vacios, la aplicaciones fue testeada y logueaba correctamente,pero no cambiaba entre fragmentos. Posteriormente he realizado el inflado decada uno de los fragmentos a través del NavController, pero al tratar de loguear la app crashea altratar de inicializar la actividad de usuario. Trás diversos testers y debugeos,aún no he conseguido solventarlo.<br/>

### Bibliografía Utilizada
:small_orange_diamond:[Documentación fragmentos](https://developer.android.com/guide/components/fragments?hl=es-419)<br/>

##  :books:Revisión del 11 de Mayo de 2020
Adjunto el enlace de la aplicacion tal cual tengo desarrollada hasta ahora y el video explicativo.<br/>
:small_orange_diamond:[Apk de la aplicación](https://github.com/Alegarse/MaipetsFCT/blob/master/Apk/app-debug.apk)<br/>
:small_orange_diamond:[Video explicativo]()

---

## Descripcion
Se trata de una app que trata de aunar en una misma aplicación la gestión de nuestras mascotas del hogar, su atención y cuidado, e implementar tambien utilidades como gestión de calendario y citas con el veterinario, control de vacunas, acceso a canal de chat de usuarios y una seccion para posibles mascotas extraviadas, en las que indicar zona y fotografía.<br/>
Todo con un control de acceso de usuario, con la posibilidad de integrarlo con redes sociales y una pequeña descripción biográfica.<br/>

## Objetivo y público
El objetivo de la aplicacion es hacer más facil y asequible que los propietarios de mascotas puedan llevar el mantenimiento y cuidado de estás de forma más organizada, de manera que sea la app quien se encarge que programar los avisos necesarios, ya sean de compra de alimento, recordatorios de paseos, citas programadas, además de ofrecer que puedan interactuar con otros propietarios, compartiendo así su amor y hobby por las mascotas.<br/>
Así, el publico objetivo de esta aplicaciones son los amantes de las mascotas, e inclusive personas que estén pensando adquirir una, puedan registrarse y charlar con otros usuarios que les darán recomendaciones.<br/>

## Aspecto Técnico
De forma técnica y actualmente de manera somera, ya que no está del todo diseñada la idea que conformará el proyecto, esté constará de una aplicación desarrollada bajo la aplicación Android Studio, haciendo hincapíe en un diseño apoyado en fragmentos,de forma que sea mas amigable e intuitiva la interfaz, desde la cual se podrá acceder a las principales funciones.<br/>
La interfaz iniciará e una actividad de logeo, con copciones de registro y restaruación de contraseña, la cual desembocará en la actividad principal de la app, que se contendrála página de usuario, con uso a través de cards, reclycerViews y modales para mostrar su contenido, todo ello sujeto al desarrollodela app, por lo que podria modificarse si se estima que así seoptimizase dicha app.<br/>
Constará también de un menú contextual, el cual servirá de enlace a la gestión del perfil de usuario, información tecnica de la aplicación y omo via de deslogeo.<br/>
Dicho perfil de usuario abandonará la aplicación nativa para desarrollarse en un entorno Web, desde el que se accederá a los datos de dicho usuario,con edición, inserción y borrado de datos.<br/>
Inclusive se realizará para la administración de usuarios por el desarrollador una aplicación web, similar a la de getión de usuario, pero que solo sera de acceso a este último.<br/>

## Medios a utilizar
Nos valdremos de los siguientes medios:<br/>
Android Studio para el desarrollo de la app<br/>
Visual Studio Code para el desarrollo de la web<br/>
InkScape para el diseño de logos<br/>
Contenido opensource de la red<br/>
