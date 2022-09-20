import java.io.FileOutputStream

val fileName = "gradle.properties"
fun main(args: Array<String>){
    var text = "\n"
    val map = mapOf("HTTP_PROXY" to "http", "HTTPS_PROXY" to "https")
    System.getenv().forEach { (key, value) ->
        val _key = key.toUpperCase()
        if (_key in map){
            val base = map[_key]
            val (val1,val2) = value.split('@')
            val (_,val4) = val1.split("//")
            val (userName,password) = val4.split(':')
            val (host,port) = val2.split(':')
            text += "systemProp.$base.proxyHost=$host\n"
            text += "systemProp.$base.proxyPort=$port\n"
            text += "systemProp.$base.proxyUser=$userName\n"
            text += "systemProp.$base.proxyPassword=$password\n"
        }
    }
    FileOutputStream(fileName, true).bufferedWriter().use { writer ->
        writer.append(text)
    }
}