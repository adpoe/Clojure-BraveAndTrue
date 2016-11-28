(ns shire.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def number 101)
  (def numplus (add100 number))
  (println  "Hello, World!")
  (println (str numplus)))

;; vector of maps
(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  ;; de-structure the input
  ; loop binds asym-body-parts to the local variable 'remaining....' --> to start, then we begin pulling values out of it by head/tail
  ; it also binds final-body-parts vector to an empty vector, to start
  ; goal is to fill that empty vector and eventually return
  ;;; learned:  you can declare variables for a loop, like this, and recur on them
 (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    ;; recursively build the symmetrize part list
    (if (empty? remaining-asym-parts)
      ;; if the remaining parts section is empty, return the final list we've built
      final-body-parts
      ;; otherwise push the unique parts set into final-body-parts
      ;; and recur on the tail of the list
      (let [[part & remaining] remaining-asym-parts] ; this pulls one value out of remaining-asym-parts, each time --> stores part as head, remaining as tail
        (recur remaining ; recur on the tail
               (into final-body-parts ; put valid sets into final-body-parts
                     (set [part (matching-part part)])))))))

;; generate all parts
(symmetrize-body-parts asym-hobbit-body-parts)

;; recursive printer
(defn recursive-printer
  ([]
   (recursive-printer 0))
  ([iteration]
   (println iteration)
   (if (> iteration 3)
     (println "Goodbye!")
     (recursive-printer (inc iteration)))))

(defn matching-part
  [part]
  ; only replace left with right IFF the part name begins with 'left',
  ; after we de-reference it
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

;; pattern --> process each element in a sequence and build a result is very common. so it's encapsualted in the reduce command
; i.e. - sum with a reduce 
(reduce + [1 2 3 4])
; can also take an optional, initial, accumulator value
(reduce + 15 [1 2 3 4])

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and a :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)]))) ;; from fn --> here is an anonymous function that takes 2 args, and places matching parts into the final parts list
          [] ;; okay, final-body-parts binds to this empty list in the reduce
          asym-body-parts)) ;; and asm-body-parts binds to part, n times, for the reduce

;; ^^ tricky

;; hobbit violence! 
(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts) ; bind result of symmetrize to sym-parts
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)] ;; of bindings
    ;; start of let expression --> expression is a loop
    (loop [[part & remaining] sym-parts ; --> get a head and tail from sym-parts
           accumulated-size (:size part)]  ; pull size out of the head of list
      ;; end loop bindings and start the expression
      (if (> accumulated-size target) ; accumulated size starts @ size of first part
        part ; see book diagram for explanation of what's intended here
        (recur remaining (+ accumulated-size (:size (first remaining)))))))) ;; otherwise, keep recurring.. first arg is remaining, second is the addition of the head's size to the accumulator

;; EXERCISES
; ex1
; Use the str, vector, list, hash-map, and hash-set functions.
(def me "tony")
(str "hello there " me)

(vector 1 2 3 4)

'(1 2 3 4)

(hash-map :name "tony" :job "programmer")

(hash-set :name "tony" :name "tony" :name "tony")
(hash-set :name "tony" :name1 "toni" :name2 "toney")

; ex2
; Write a function that takes a number and adds 100 to it.
(defn add100
  [number]
  (+ number 100))

; ex3
; Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction:
(defn dec-maker
  "Create a custom decrementor"
  [dec-by]
  #(- % dec-by)) ;; return an anonymous fn that decerements by the number we input

(def dec3 (dec-maker 3))
; ex4
;Write a function, mapset, that works like map except the return value is a set
(defn mapset
  "Works like map, except the return value is a set"
  [fn coll]
  (def mapped-vals (map fn coll))
  (set mapped-vals))

; ex5 
; Create a function that’s similar to symmetrize-body-parts except that it has to work with weird space aliens with radial symmetry. Instead of two eyes, arms, legs, and so on, they have five.
(defn alien-matching-parts
  [part]
  ; only replace left with right IFF the part name begins with 'left',
  ; after we de-reference it
  ; Return a vector of all 5 parts we need, instead
  (if (re-find #"^left" (:name part))
    [{:name (clojure.string/replace (:name part) #"^left-" "1-")
   :size (:size part)}
  {:name (clojure.string/replace (:name part) #"^left-" "2-")
   :size (:size part)}
  {:name (clojure.string/replace (:name part) #"^left-" "3-")
   :size (:size part)}
  {:name (clojure.string/replace (:name part) #"^left-" "4-")
   :size (:size part)}
  {:name (clojure.string/replace (:name part) #"^left-" "5-")
   :size (:size part)} ]
    [part]))

(defn alien-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  ;; de-structure the input
  ; loop binds asym-body-parts to the local variable 'remaining....' --> to start, then we begin pulling values out of it by head/tail
  ; it also binds final-body-parts vector to an empty vector, to start
  ; goal is to fill that empty vector and eventually return
  ;;; learned:  you can declare variables for a loop, like this, and recur on them
 (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    ;; recursively build the symmetrize part list
    (if (empty? remaining-asym-parts)
      ;; if the remaining parts section is empty, return the final list we've built
      final-body-parts
      ;; otherwise push the unique parts set into final-body-parts
      ;; and recur on the tail of the list
      (let [[part & remaining] remaining-asym-parts] ; this pulls one value out of remaining-asym-parts, each time --> stores part as head, remaining as tail
        (recur remaining ; recur on the tail
               (into final-body-parts ; put valid sets into final-body-parts
                       (set [(alien-matching-parts part)])))))))

;; ex6
;Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5. The new function should take a collection of body parts and the number of matching body parts to add. If you’re completely new to Lisp languages and functional programming, it probably won’t be obvious how to do this. If you get stuck, just move on to the next chapter and revisit the problem later.
; Copying from: https://github.com/cataska/braveclojure/blob/master/src/braveclojure/excercises/ch3.clj 
; to gain understanding, for now...
(defn matching-part
  [part index]
  {:name (clojure.string/replace (:name part) #"^1-" (str index "-")) ; replace the number in the name with whichever index we are on
   :size (:size part)}) ; grab the size from the part passed in


(defn make-body-parts
  [part count] ; take a part and a count for how many parts to make
  (loop [i 1   ; define variables to loop with. i=index, results=[] empty vector @start
         results []]
    (if (> i count)  ; so, if we've indexed higher than count, end
      results        ; and return the results vector
      (recur (inc i) (conj results (matching-part part i)))))) ; recur on the index+1 and the vector, with the next part's index-name added

(defn symmetrize-body-parts
  [asym-body-parts number]  ; take the body parts list and a generalized number
  (reduce (fn [final-body-parts part] ; run a reduce, with an anonymous function that runs our make-body-parts function with that number of parts  
            (into final-body-parts (set (make-body-parts part number)))) ;; a make a set out of our results, so there are only unique values, and them place them into final body parts
          [] ;; bind final-body-parts to the empty vector, []
          asym-body-parts)) ;; bind each 'part' to incremental indices of the input arg, asym-body-parts, because we are running a reduce
