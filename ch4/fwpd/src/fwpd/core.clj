(ns fwpd.core) ; establishes a namespace
(def filename "suspects.csv") ; define a var for the csv we've created

(def vamp-keys [:name :glitter=index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int}) ; cool, you can make the values of a map like this actually functions!

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

; parse function
(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",") ; map a split on "," over the newline split list
       (clojure.string/split string #"\n"))) ; split the whole input string on newlines

; take a seq of vectors and combines it with your vamp keys to create maps
(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))
