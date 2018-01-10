(ns bofm-tracker.core)

(def books
  [
   {:name "1 Nephi" :chapters 22 :url-id "1-ne"}
   {:name "2 Nephi" :chapters 33 :url-id "2-ne"}
   {:name "Jacob" :chapters 7 :url-id "jacob"}
   {:name "Enos" :chapters 1 :url-id "enos"}
   {:name "Jarom" :chapters 1 :url-id "jarom"}
   {:name "Omni" :chapters 1 :url-id "omni"}
   {:name "Words of Mormon" :chapters 1 :url-id "w-of-m"}
   {:name "Mosiah" :chapters 29 :url-id "mosiah"}
   {:name "Alma" :chapters 63 :url-id "alma"}
   {:name "Helaman" :chapters 16 :url-id "hel"}
   {:name "3 Nephi" :chapters 30 :url-id "3-ne"}
   {:name "4 Nephi" :chapters 1 :url-id "4-ne"}
   {:name "Mormon" :chapters 9 :url-id "morm"}
   {:name "Ether" :chapters 15 :url-id "ether"}
   {:name "Moroni" :chapters 10 :url-id "moro"}
   ])

  
(defn join-map [m]
  (if m
    (apply str " " (interpose " " (map (fn [[k v]] (str (name k) "=\"" v "\"")) m)))
    ""))

(defn link-tag
  ([name] (link-tag name nil ""))
  ([name  body] (link-tag name nil body))
  ([name attrs body]
   (if body
     (str "<" name (join-map attrs) ">"
          body
          "</" name ">")
     (str "<" name (join-map attrs) ">"))))

(defn tag
  ([name] (tag name nil ""))
  ([name  body] (tag name nil body))
  ([name attrs body]
   (if body
     (str "<" name (join-map attrs) ">"
          body
          "</" name ">")
     (str "<" name (join-map attrs) "/>"))))

(defn chapterxx [b num]
  (str num))

(defn chapter [b num]
  (let [id (str (:url-id b) "-p" num)]
    (tag "label" {:class "checkbox-inline" :for id}
         (str
          (tag "input" {:class "checkbox"
                        :type "checkbox"
                        :value ""
                        :id id} nil)
          num))))
  
  
  
(defn chapters [b]
  (apply str (map #(chapter b (inc %)) (range (:chapters b)))))

(defn book [b]
  (let [head-id (str "head" (:url-id b))
        collapse-id (str "collapse" (:url-id b))]
    (tag "div" {:class "card"}
         (str
          (tag "div" {:class "card-header" :role "tab" :id head-id}
               (tag "h2" {:class "mb-06666"}
                    (tag "a" {:data-toggle "collapse"
                              :href (str "#" collapse-id)
                              :role "button"
                              :aria-expanded "false"
                              :aria-controls collapse-id}
                         (:name b))))
          (tag "div" {:id collapse-id
                      :class "collapse"
                      :role "tabpanel" 
                      :aria-labelledby head-id
                      :data-parent "#accordion"}
               (tag "div" {:class "card-body"}
                    (chapters b)))))))

(defn summary-content []
  (str
   (tag "h2" "Goal: read Book of Mormon by July 1")
   (tag "h3" (str "Days remaining: " (tag "span" {:id "days"} "counting..."))) 
   (tag "h3" (str "Chapters per day: " (tag "span" {:id "chapters"} "1"))) 
   (tag "h2" "Progress")
   (tag "h3" (str "Read:" (tag "span" {:id "read"} "0")))
   (tag "h3" (str "Remaining:" (tag "span" {:id "remaining"} "239")))
   (tag "h3" (str "Percent:" (tag "span" {:id "percent"} "0") "%"))))

(defn head [] 
  (tag "head"
       (str
        (tag "title" "2018 FIRM Reading Tracker")
        (link-tag "meta" {:charset "UTF-8"} nil)
        (link-tag "link" {:rel "stylesheet"
                          :href "css/bofmtracker.css"}
                  nil)
        (link-tag "link" {:rel "stylesheet"
                          :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"}
                  nil)
        (tag "script" {:src "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"} "")
        (tag "script" {:src "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.6/umd/popper.min.js"} "")
        (tag "script" {:src "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"} "")
        (tag "script" {:src "https://cdn.auth0.com/js/auth0/9.0.1/auth0.min.js"} "")
        (tag "script" {:src "js/bofmtracker.js"} ""))))

(defn nav []
  (tag "nav" {:class "navbar navbar-default"}
       (tag "div" {:class "container-fluid"}
            (tag "div" {:class "navbar-header"}
                 (str
                  (tag "a" {:class "navbar-brand"
                            :href "#"}
                       "2018 FIRM Reading Tracker")
                  (tag "button" {:id "btn-home-view"
                                 :class "btn btn-primary btn-margin"}
                       "Home") 
                  (tag "button" {:id "btn-login"
                                 :class "btn btn-primary btn-margin"}
                       "Login") 
                  (tag "button" {:id "btn-logout"
                                 :class "btn btn-primary btn-margin"}
                       "Logout")
                  (tag "p" {:id "login-status" :class "navbar-brand"} ""))))))

(defn -main []
  (print
   (str
    "<!DOCTYPE" "html>\n"
    (tag "html"
         (str
          (head)
          "\n"
          (tag "body"
               (tag "div" {:class "content"}
                    (str
                     (nav)
                     (tag "main" {:class "container"}
                          (tag "div" {:id "home-view"}
                               (tag "div" {:id "home-content" :class "row"}
                                    (let [books (map book books)]
                                      (str
                                       (tag "div" {:class "col-4" :id "summary"} (summary-content))
                                       (tag "div" {:id "accordian" :role "tablist" :class "col-8"}
                                            (apply str  books)))))))))))))))


