;; fn examples
(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOOOMED!")))


(defn too-enthusiastic
  "Return a cheer that might be a bit too enthusiasitc"
  [name]
  (str "OH. MY GOD! " name " YOU ARE MOST DEFINITELY THE BEST " "MAN SLASH WOMAN EVER I LOVE YOU AND WE SHOULD UN AWAY SOMEWHERE"))


;; arity overloading
(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type]
     (str "I " chop-type " chop " name "! take that!"))
  ;; if ony supply one arg, call self with type of chop called karate
  ([name]
     (x-chop name "karate")))

(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper "!!!"))

(defn codger
  ;; & allows for variable args, and puts them all in a list with the name specified
  [& whippersnappers]
  (map codger-communication whippersnappers))

;; can mix rest parameter (&) and normal params, but rest param MUST be last
(defn favorite-things
  [name & things]
  (str "Hi, " name ", here are my favorite things: "
       (clojure.string/join ", " things)))


;; destructuring
;;  --> return first element of a collection
(defn my-first
  [[first-thing]] ; NNotice that first-thing is within a vector
  first-thing)

;; if you take in a vector or list, you can name as many elements as you want and also use rest parametsers

(defn chooser
  [[first-choice second-choice & unimportant choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choice. " "Here they are in case you need to cry over them: ") (clojure.string/join ", " unimportant-choices)))


;; can also destructure maps
(defn announce-treasure-location
  [{lat :lat lng :lng}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

;; can also use shorter syntax
;; --> [{:keys [lat lng}]

;; anonymous fns
(fn [param-list]
  fn-body)

(map (fn [name] (str "Hi, " name))
     ["Darth Vader" "Mr. Magoo"])

;; more compact way to do same thing
#(* % 3)
; percent sign is the arg passed in
; and we can have multiples
(#(str %1 " and " %2) "cornbread" "butter beans")

;; can also pass a rest parameters with %&


;; returning functions
;  --> these are _closures_, meaning they have all same variables ins cope as when the function was created. standard example:
(defn inc-maker
  "Create a custom incrementer"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))

(inc3 7)
