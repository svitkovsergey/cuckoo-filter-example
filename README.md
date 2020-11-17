# Streaming example
Докер-образ: `sbt docker:publishLocal`, экспоузил 8080 порт
## Preface
Специально взял за основу http4s с fs2, тк почти не работал ранее с этими библиотеками.
На akka-http с этим было бы куда [проще](https://doc.akka.io/docs/akka-http/current/routing-dsl/source-streaming-support.html#consuming-json-streaming-uploads), но мне захотелось поэкспериментировать (не могу сказать, что я доволен тем, что получилось, но на это мне, по крайней мере, интересно получить фидбэк и предложения по возможным улучшениям).

p.s.: **circe-generic-extras, судя по всему, нельзя подружить с http4s-circe, поэтому, пожалуйста, используйте в json'ах camel case** 
## Оценка временной сложности решения и некоторые рассуждения о репликации
За основу взял 2 TrieMap'ы для хранения состояния, одну для хранения количества уникальных значений, вторую для запоминания уже встретившихся значений (сюда, наверное, подошел бы и обычный сет, но я подумал, что может быть многовато коллизий).
Таким образом, каждый элемент обрабатывается за константное время. Плюс, можно сделать снэпшот за константное время, что может быть полезно при запуске решения в несколько реплик.

В случае с запуском нескольких реплик можно сохранять timeuuid последнего обновления и снэпшот состояния. 
В неприятных ситуациях можно будет помержить по last writer wins. 