class RtmApi {

    final String REST_URL = "https://api.rememberthemilk.com/services/rest/",
                 AUTH_URL = "https://www.rememberthemilk.com/services/auth/"

    final String apiKey, sharedSecret, perms
    String authToken = null
    boolean verbose = true

    RtmApi(key, secret, perms='delete'){
        this.apiKey = key
        this.sharedSecret = secret
        this.perms = perms
    }

    RtmApi withAuthToken(String authToken)  {this.authToken = authToken; return this}
    RtmApi withVerbose(boolean verbose)     {this.verbose = verbose; return this}

    /// general stateless utils methods
    def md5(String str) { java.security.MessageDigest.getInstance("MD5").digest(str.bytes).encodeHex().toString() }
    def encode(str){ URLEncoder.encode(str.toString(), "UTF-8") }
    def log(...objects){ if (verbose) objects.each { println it } }
    def toUrl(String endpoint, Map params=[:]) { new URL([endpoint, params.collect{k,v -> encode(k)+ "=" + encode(v)}.join("&")].join("?")) }
    def prettyXml(node) {groovy.xml.XmlUtil.serialize(node)}


    // rtm specifics
    Map sign(Map params) {[
        *        : params,
        api_sig  : md5(new TreeMap(params).inject(new StringBuilder(sharedSecret)){ sb, k, v -> sb << k << v }.toString())
    ]}

    def method(String name, params =[:]) {
        def p = [method: name, *:params, api_key: apiKey]
        if (authToken) { p.auth_token = authToken }

        def url = toUrl(REST_URL, sign(p))
        def responseText = url.text
        log url, responseText

        new XmlSlurper().parseText(responseText)
    }

    def getAuthUrlForWebApp(){
        toUrl(AUTH_URL, sign(api_key: apiKey, perms:perms))
    }

    def getFrobAndAuthUrlForDesktop(){
        def rsp = this.method('rtm.auth.getFrob')
        if (rsp.@stat == 'ok') {
            return [
                frob: rsp.frob.text(),
                url:  toUrl(AUTH_URL, sign(api_key: apiKey, perms:perms, frob: rsp.frob))
            ]
        }
        throw new RuntimeException("Could not get Frob : \n" +  prettyXml(rsp))
    }

    def getAuthToken(String frob){
        method('rtm.auth.getToken', [frob:frob])
    }

    def checkAuthToken() {
        if (!authToken) throw new RuntimeException("no AuthToken set on the client")
        method('rtm.auth.checkToken')
    }
}
