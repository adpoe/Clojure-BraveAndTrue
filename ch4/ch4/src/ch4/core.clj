(ns ch4.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; examples
(defn titleize
  [topic]
  (str topic " for the Brave and True"))

;; map examples
; can give it multiple collections
(map str ["a" "b" "c"] ["A" "B" "C"])

; vampire example
(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})
(map unify-diet-data human-consumption critter-consumption)
; think: can define things for any single item (or set of items) and then map the function over two collections (or n-collections) of that type, as long as they are in the correct argument order, and you will return a list with a collection of whatever data structures we need.. cool.

; can pass map a collection of cuntions
; i.e. -- if want to perform one set of calculations on some different collections of numbers
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [80 1 44 13 6]) ;; Here, stats maps our input collection of numbers over a series of functions, all calculations, and gives us back a list which is the statistcal summary --> the result of each function's calculations on our collection, in order.


;; can also map to retrieve the value associated with a keyword, from a collection of map data structures. Because keywords can be used as fn's, we can do this succinctly.
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])
(map :real identities) ;; gets a list of all real identities only!

;; and, onto REDUCE
; we can transform a map's values, producing a NEW map with the same keys, but updated values
(reduce (fn [new-map [key val]] ; define the values that our fn will use
          (assoc new-map key (inc val))) ; define the function itself
        {} ; the new map value, starts out as an empty map
        {:max 30 :min 20}) ; the key-value store, which is dereferenced

;; assoc take three args
; 1. a map
; 2. a key
; 3. a value
; and it derives a NEW map from the map given, by associating the given key with the given value

; can also use reduce to filter out keys from a map, based on their value
(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val) ;then
            new-map)) ; else
        {} ; this is new map
        {:human 4.1 ; here is where key and val come, each iteration
         :critter 3.9})

; take away --> reduce is more flexible than seems at first.
; can use it whenever we want to derive a NEW VALUE from a seqable data structure

; take, drop, take-while, and drop-while
; take and drop --> each take a number and sequence
(take 3 (range 1 10))
(drop 3 (range 1 10))

; take-while and drop-while each take a predicate function (check for T/F)
(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

; let's get just january and february's data
(take-while #(< (:month %) 3) food-journal)
; and we can drop the same data
(drop-while #(< (:month %) 3) food-journal)

; using these together, we can get ONLY data for Feb (2) and March (3)
(take-while #(< (:month %) 4) 
            (drop-while #(< (:month %) 2) food-journal))

; filter and some
; use filter to return all elements of a sequence that test true for a predicate function
(filter #(< (:human %) 5) food-journal)
; this works too --> but take-while can be more efficient if data is sorted!
(filter #(< (:month %) 3) food-journal)

; use some to check whether a collection has ANY values that test true for some predicate function
(some #(> (:critter %) 5) food-journal) ; nil
(some #(> (:critter %) 3) food-journal) ; true

; return the true entry from above
(some #(and (> (:critter %) 3) %) food-journal)

; sort and sort-by
; ascending order
(sort [3 1 2])

; use a key-function to sort
(sort-by count ["aaa" "c" "bb"])

; concat --> append members of one sequence to end of another
(concat [1 2] [3 4])

; LAZY SEQ
; like haskell, clojure has lazinesss. many functions return a lazy-seq, whose members aren't compute until you try to access them --> allows construction of infinite sequences, without worry

; Vampmatic 300 example
(def vampire-database 
  ; a map of maps
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record)) ; use and to check if all 3 are true

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

; Infinite Sequences
(concat (take 8  (repeat "na")) ["Batman!"])

(take 3 (repeatedly (fn [] (rand-int 10))))

(defn even-numbers
  ([] (even-numbers 0)) ; base case, if no args. If we call with no args, call self with value u
  ([n] (cons n (lazy-seq (even-numbers (+ n 2)))))) ; recursive case, cons (n + 2) onto the list

(take 10 (even-numbers))

;; The Collection Abstraction
; related to the sequence abstraction. all of clojure's data structures take part in both. 
; Sequence is about operating on members individually
; Collection is about operating on the data structure as a whole

; into
; many seq fn's return a seq, rather than the original data structure. want to convert the return value back to original, and into lets you do that.
(map identity {:sunlight-reaction "Glitter!"})
(into {} (map identity {:sunlight-reaction "Glitter!"}))
; also works with vectors []
; and sets #{}
; and .. more

; first arg of into doesn't have to be empty
(into {:favorite-emotion "gloomy"} [[:sunlight-reaction "Glitter!"]])
(into ["cherry"] '("pine" "spruce"))

; overall, use this to take 2 collections, and all elements from 2nd into 1st


; conj
; add free element(s) into some collection
(conj [0] 1)
(conj [0] 1 2 3 4)

; similarities --> you can define conj in terms of into
(defn my-conj
  [target & additions]
  (into target additions))
; common idom. often we find two functions that do the same thing, but one takes a rest parm (conj) and the other takes a seqable data structure

; Function Functions
; i.e. - max take any # of args, and returns greatest #
(max 0 1 2)
; but this doesn't work
(max [0 1 2])
; however, this will
(apply max [0 1 2])
; 'lifting' in Haskell

; define into in terms of conj, using apply
(defn my-into
  [target additions]
  (apply conj target additions))
; i.e.
(my-into [0] [1 2 3])


; partial
; takes a fn and any number args, then returns a new function, partially applied. When you call the returned function, it calls the original function, with the original arguments you supplied it, along with the new arguments
(def add10 (partial + 10))
(add10 3)

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))
(add-missing-elements "unobtainium adamantium")

; here's how partial works
(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))

(def add20 (my-partial + 20))
(add20 3)

; generally --> want to use partials when you find that you're repeating the same combination of function and arguments in many different conexts. Example is below.
(defn lousy-logger
  [log-level message]
  (condp = log-level ; condp is like a switch, it binds to the input variable and chooses a path, based on what the input variable is equal to
    :warn (clojure.string/lower-case message)
    :emergency (clojure.string/upper-case message)))

(def warn (partial lousy-logger :warn))
(def emergency (partial lousy-logger :emergency))
(warn "Red light ahead")
(emergency "Red light ahead")
; here, calling (warn ...) is identical to calling (lousy-logger :warn ...)

; complement
(defn identify-humans
  [social-security-numbers]
  (filter #(not (vampire? %))
          (map vampire-related-details social-security-numbers)))

; we can acheive same thing using the complement (boolean negation)
(def not-vampire? (complement vampire?))
(defn identify-humans
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details social-security-numbers)))

; implementation of complement
(defn my-complement
  [fun]
  (fn [& args]
    (not (apply fun args)))) ; apply whatever function was passed in to the arg list and negate it

(def my-pos? (complement neg?))
(my-pos? 1)
(my-pos? -1)


