package main

import (
	"embed"
	"flag"
	"fmt"
	"html/template"
	"io"
	"log"
	"net/http"
)

//go:embed public
var publicContent embed.FS

//go:embed templates
var templates embed.FS

type Category struct {
	Name     string
	ColorHsl string
	Score    int
	IconPath string
}

func iconPath(categoryName string) string {
	return fmt.Sprintf("/static/public/images/icon-%s.svg", categoryName)
}

type ResultsSummaryData struct {
	Categories   []Category
	TotalScore   int
	UpperPercent int
}

// starts webserver that serves html page for exercise
func main() {
	// serves public static resources: bundled images, fonts, css
	// visible on http://localhost:8080/static/public/some-text.txt
	http.Handle("/static/", http.StripPrefix("/static/", http.FileServer(http.FS(publicContent))))

	http.HandleFunc("/hello", func(w http.ResponseWriter, req *http.Request) {
		io.WriteString(w, "This is temporary here, hello")
	})

	resultsData := ResultsSummaryData{
		Categories: []Category{
			{Name: "Reaction", ColorHsl: "0deg 100% 67%",
				Score: 80, IconPath: iconPath("reaction")},
			{Name: "Memory", ColorHsl: "39deg 100% 56%",
				Score: 92, IconPath: iconPath("memory")},
			{Name: "Verbal", ColorHsl: "166deg 100% 37%",
				Score: 61, IconPath: iconPath("verbal")},
			{Name: "Visual", ColorHsl: "234deg 85% 45%",
				Score: 72, IconPath: iconPath("visual")},
		},
		TotalScore:   76,
		UpperPercent: 65,
	}

	// main page with results summary
	http.HandleFunc("/", func(w http.ResponseWriter, req *http.Request) {
		templateName := "templates/summary-component.gohtml"
		tmpl := template.Must(template.ParseFS(templates, "templates/index.gohtml", templateName))
		if err := tmpl.Execute(w, resultsData); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	})

	var port int
	var host string
	flag.IntVar(&port, "p", 8080, "Specify port for server to start on")
	flag.StringVar(&host, "h", "localhost", "Specify host for server to start on")
	flag.Parse()

	address := fmt.Sprintf("%s:%d", host, port)
	log.Printf("starting server on %s", address)
	log.Fatal(http.ListenAndServe(address, nil))
}
