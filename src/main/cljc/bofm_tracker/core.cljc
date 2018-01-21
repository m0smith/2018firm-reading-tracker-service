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

(def wards
  [
   "2nd"
   "3rd"
   "4th"
   "5th"
   "6th"
   "9th"
   "10th"
   "Mount Ensign"
   ])

(defn attr [[k v]]
  (if v
    (str (name k) "=\"" v "\"")
    (name k)))

(defn join-map [m]
  (if m
    (apply str " " (interpose " " (map attr m)))
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

(defn tag-body [name attrs body]
   (if body
     (str "<" name (join-map attrs) ">"
          body
          "</" name ">")
     (str "<" name (join-map attrs) "/>")))

(defn tag
  ([name] (tag name nil ""))
  ([name  body] (tag name nil body))
  ([name attrs body] (tag-body name attrs body))
  ([name attrs b1 b2] (tag-body name attrs (str b1 b2)))
  ([name attrs b1 b2 b3] (tag-body name attrs (str b1 b2 b3)))
  ([name attrs b1 b2 b3 b4] (tag-body name attrs (str b1 b2 b3 b4)))
  ([name attrs b1 b2 b3 b4 b5] (tag-body name attrs (str b1 b2 b3 b4 b5))))

(defn chapter [b num]
  (let [id (str (:url-id b) "-p" num)]
    (tag "label" {:class "btn btn-xlarge btn-primary btn-chapter" :for id}
         (str
          (tag "input" {:autocomplete "off" :type "checkbox" :class "chapter-cb"
                        :id id} nil)
          (str " " num)))))
  
  
  
(defn chapters [b]
  (tag "div" { :data-toggle "buttons"}
       (apply str (map #(chapter b (inc %)) (range (:chapters b))))))

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
        (link-tag "link" {:rel "shortcut icon" :href "favicon.ico"
                          :type "image/x-icon"} nil)

        (link-tag "link" {:rel "stylesheet"
                          :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"}
                  nil)
        (tag "script" {:src "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"} "")
        (tag "script" {:src "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.6/umd/popper.min.js"} "")
        (tag "script" {:src "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"} "")
        (tag "script" {:src "https://cdn.auth0.com/js/auth0/9.0.1/auth0.min.js"} "")
        (link-tag "link" {:rel "stylesheet"
                          :href "css/bofmtracker.css"}
                  nil)
        (tag "script" {:src "js/bofmtracker.js"} ""))))


(defn progress []
  (tag "div" {:class "container" :id "summary-view"}
       (tag "h4" nil (str "Target July 1: " (tag "span" {:id "days"} "0") " days remaining"))
       (tag "div" {:class "progress"}
            (tag "div" {:id "personal-progress-bar" :class "progress-bar" 
                        :role "progressbar" :aria-valuenow "0"
                        :aria-valuemin "0"
                        :aria-valuemax "239" :style "width:0%"}
                 "<span>0%</span>"))))


(defn registration-item [id desc class]
  (let [i (str "attendee-group-" id)]
    (tag "label" {:class "btn btn-primary btn-xlarge" :for i}
         (tag "input" {:type "radio" :name "user_type" :class (str "attendee-group-cb attendee-group-"
                                                                   class)
                       :id i :value id} nil)
       desc)))

(defn ward-option [name]
  (tag "option" {} name))

(defn registration []
  (let [ids ["youth", "leader", "other", "none"]
        classes ["youth", "non-youth", "non-youth", "non-youth"]
        descs ["a youth", "a leader", "other", "not attending"]]
    (tag "div" {:class "container" :id "registration-view"}
         (tag "form" {:class "form" :id "registration-form" :data-toggle "buttons"}
              (tag "h3" {} "I am attending 2018 FIRM as")
              (tag "div" {:class "btng" :data-toggle "buttons"}
                   (apply str (map registration-item ids descs classes)))
              (tag "div" {:class "form-group" :id "ward-select-view"}
                   (tag "h3" {:for "select-ward"} "Select your ward:")
                   (tag "select" {:class "form-control" :id "select-ward" :name "ward"}
                        (apply str (map ward-option wards))))
              (tag "div" {}
                   (tag "button" {:id "registration-submit" :disabled nil
                                  :class "disabled btn" :type "button"} "Complete"))))))


(defn ward-progress [ward]
  (tag "div" {:class "container ward-progress-view" :id (str ward "-progress-view")}
       (tag "h4" nil ward)
       (tag "div" {:class "progress"}
            (tag "div" {:id (str ward "-progress-bar") :class "progress-bar" 
                        :role "progressbar" :aria-valuenow "0"
                        :aria-valuemin "0"
                        :aria-valuemax "239" :style "width:0%"}
                 "<span>0%</span>"))))


(defn tally-view []
  (tag "div" {:id "tally-view" :class "container"}
       (apply str (map ward-progress wards))))
    
  
(defn nav []
  (tag "nav" {:class "navbar navbar-default"}
       (tag "div" {:class "container-fluid"}
            (str
             (tag "div" {:class "navbar-header"}
                  (str
                   (tag "a" {:class "navbar-brand"
                             :href "#"}
                        "2018 FIRM Reading Tracker")
                   (tag "button" {:id "btn-home-view"
                                  :class "btn btn-primary btn-margin btn-xlarge"}
                        "Home") 
                   (tag "button" {:id "btn-tally-view"
                                  :class "btn btn-primary btn-margin btn-xlarge"}
                        "Tally") 
                   (tag "button" {:id "btn-login"
                                  :class "btn btn-primary btn-margin btn-xlarge"}
                        "Login") 
                   (tag "button" {:id "btn-logout"
                                  :class "btn btn-primary btn-margin btn-xlarge"}
                        "Logout")
                   (tag "p" {:id "login-status" :class "navbar-brand"} "Please login.")
                   (tag "div" {:id "logout-status" :class "navbar-brand"}
                        (str
                         (tag "img" {:id "user-photo" :height "42"} nil)
                         (tag "p" {:id "logout-status-text"} "")))
                   (tag "p" {:id "error-view"} "")))))))

(defn -main []
  (print
   (str
    "<!DOCTYPE html>\n"
    (tag "html" nil
          (head)
          "\n"
          (tag "body"
               (tag "div" {:class "content"}
                    (nav)
                    (registration)
                    (tally-view)
                    (tag "main" {:class "container"}
                         (progress)
                         (tag "div" {:id "home-view" :class "container"}
                              (tag "div" {:id "home-content" :class "row"}
                                   (let [books (map book books)]
                                     (str
                                      (tag "div" {:id "accordian" :role "tablist" :class "col-12"}
                                           (apply str  books)))))))))))))


