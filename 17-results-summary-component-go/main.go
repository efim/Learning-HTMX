package main

import (
	"embed"
	"html/template"
	"io"
	"log"
	"net/http"
)

//go:embed public
var publicContent embed.FS

//go:embed templates
var templates embed.FS

// starts webserver that serves html page for exercise
func main() {
	// serves public static resources: bundled images, fonts, css
	// visible on http://localhost:8080/static/public/some-text.txt
	http.Handle("/static/", http.StripPrefix("/static/", http.FileServer(http.FS(publicContent))))

	http.HandleFunc("/hello", func(w http.ResponseWriter, req *http.Request) {
		io.WriteString(w, "This is temporary here, hello")
	})

	// main page with results summary
	http.HandleFunc("/", func(w http.ResponseWriter, req *http.Request) {
		templateName :=  "templates/summary-component.gohtml"
		tmpl := template.Must(template.ParseFS(templates, templateName))
		tmpl.Execute(w, nil)
	})

	log.Print("starting server on default :8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}
