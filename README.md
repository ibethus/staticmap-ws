# Static Map Web Service 

Service web permettant de générer des images de cartes statiques à la volée à partir de paramètres (centre, zoom, marqueurs, chemins) ou d’un fichier GPX, en s’appuyant sur la bibliothèque Java `io.github.ibethus.staticmap:staticmap`.

## Objectifs
- Fournir une alternative libre au Google Static Maps API.
- Générer rapidement des PNG/JPEG (ou formats supportés) via un simple appel HTTP POST.
- Permettre l’overlay de traces GPX, de marqueurs et de chemins stylés.
- Support d’un fournisseur de tuiles personnalisé (URL template).

## Fonctionnalités principales
- Endpoint unique: `POST /staticmap`
- Paramètres carte: `center`, `zoom`, `width`, `height`, `scale`, `maptype`
- Téléversement d’un fichier GPX (optionnel)
- Génération d’un tracé à partir du GPX (auto-fit si `center`/`zoom` absents)

## Endpoint API (résumé)
POST /staticmap  
Content-Type: multipart/form-data  
Champs possibles (tous optionnels sauf dimensions):
- width, height: dimensions (px)
- center: "lat,lng" (si pas de GPX ou si zoom manuel requis)
- zoom: entier
- scale: 1 ou 2
- maptype: (ex: standard, satellite, ... selon implémentation interne)
- markers: liste répétée (`markers=lat,lng;color=hex;size=small|mid|large;label=X`)
- path: liste répétée (`path=enc:...` ou `path=lat1,lng1|lat2,lng2;stroke=hex;weight=4;fill=hex`)
- gpxFile: fichier GPX (input type file)
- tileProviderUrl: template (ex: `https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png`)
- format: png (par défaut), jpeg…
- backgroundColor: couleur hex fallback
- autoFit: true/false (si true et GPX ou path présent — calcule bbox)
- padding: marge en pixels pour autoFit

La réponse est une image binaire (Content-Type selon format).

## Exemple curl (GPX)
```bash
curl -X POST http://localhost:8080/staticmap \
  -F "width=800" \
  -F "height=400" \
  -F "gpxFile=@/chemin/trace.gpx" \
  -F "tileProviderUrl=https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" \
  -o map.png
```

## Exemple curl (sans GPX, avec marqueurs)
```bash
curl -X POST http://localhost:8080/staticmap \
  -F "width=600" \
  -F "height=300" \
  -F "center=48.8566,2.3522" \
  -F "zoom=12" \
  -F "markers=48.8584,2.2945;color=ff0000;label=E" \
  -F "markers=48.8606,2.3376;color=0000ff;label=L" \
  -o paris.png
```

## Structure (prévisionnelle)
- `src/main/java/.../api/StaticMapResource.java` (REST endpoint)
- `src/main/java/.../service/MapRenderingService.java` (logique création)
- `src/main/java/.../model/` (DTO form parsing interne)
- `src/main/java/.../tiles/` (résolution template tuiles)
- `src/main/resources/` (config Quarkus)

## Construction & Exécution
Développement (hot reload):
```bash
./mvnw quarkus:dev
```

Build JAR:
```bash
./mvnw package
java -jar target/*-runner.jar
```

Build image native (nécessite GraalVM / container):
```bash
./mvnw package -Dnative
./target/*-runner
```
