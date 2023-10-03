package main

import (
	"embed"
	"io"
	"log"
	"net/http"
)

//go:embed public
var publicContent embed.FS

// starts webserver that serves html page for exercise
func main() {
	// visible on http://localhost:8080/static/public/some-text.txt
	http.Handle("/static/", http.StripPrefix("/static/", http.FileServer(http.FS(publicContent))))
	http.HandleFunc("/", func(w http.ResponseWriter, req *http.Request) {
		io.WriteString(w, "This is temporary here, hello")
	})

	log.Print("starting server on default :8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}
