(ns ch5.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

; referential transpacency --> fn's are referentially transparenty when  they always return same value, given same args; and if they don't cause any side effects

; these are ref transparent
(+ 1 2)

(defn wisdom
  [words]
  (str words ", Daniel-san"))

(wisdom "Always bathe on Fridays")

; not ref transparent
(defn year-end-evaluation
  []
  (if (> (rand) 0.5)
    "You get a raise!"
    "Better luck next year!"))

; if fn reads from a file, it's not ref transparent, because the file's contents can change. for example. analyze-file is NOT ref transparent, but analysis is
(defn analysis
  [text]
  (str "Character count: " (count text)))

(defn analyze-file
  [filename]
  (analysis (slurp filename)))


; Living with Immutable Data Structures
; immutable data structures ensure that your code won't have side effects. Clojure's data structures are immutable. But how to get anything done?

; Recursion insead of for/while

; fn composition!
; can compose pure functions, only need to worry about their I/O relationship, and comp allows you to compose a new function from any number of other functions
; example
((comp inc *) 2 3)

; example --> use comp to retrieve character attributes in role-playing games
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

; could have also written something like this
(fn [c] (:strength (:attributes c)))
; but comp is considered more elegant, uses less code and conveys more meaning

; what to do if one of the functions you want to compose needs more than one argument? wrap it in an anonymous function!
(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))
; divide intelligence by 2
; add one
; then use the int function to round down
(spell-slots character)


; here's the same thing with comp
(def spell-slots-comp (comp int inc #(/ % 2) c-int))
; to divide by 2, we just needed to warp divisin in an anonymous fn
; and looks cleaner, simpler

; implementation
; here's an example of how comp works, using only 2 fns
(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args)))) ; basically, lift the second function into arguments list to the function, then apply f on the result...



; memoize
; we can also memoize in clojure, so that the Clojure remebers the result of a particular function call. 

; example --> here results return after 1second
(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)

(sleepy-identity "Mr Fantastico")

; vs the memoized version, where every fn call after first, returns immediately, no 1second waiting
(def memo-sleepy-identity (memoize sleepy-identity))
