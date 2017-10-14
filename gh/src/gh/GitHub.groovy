package gh

import groovy.json.JsonSlurper
import groovy.transform.Immutable

@Immutable(copyWith = true)
class GitHub {
    public final static String BASE_URL = "https://api.github.com"

    private String token
    private String owner, repo

    def getReposNames(owner = this.owner) {
        getRepos(owner).collect { it.name }
    }

    def getStarredRepos(Map requestOpts = [:], owner = this.owner) {
        final Map defaultOpts = [per_page: 10L, page: 1]
        final Map opts = defaultOpts + requestOpts

        def json = getJson("users/${owner}/starred?page=${opts.page}&per_page=${opts.per_page}")

        if (opts.fields) {
            return json.collect { it.subMap(opts.fields) }
        } else {
            return json
        }
    }

    def getRepos(owner = this.owner) {
        getJson("users/${owner}/repos")
    }

    def getTraffic(owner = this.owner, repo = this.repo) {
        getJson("repos/${owner}/${repo}/traffic/views")
    }

    def getClones(owner = this.owner, repo = this.repo) {
        getJson("repos/${owner}/${repo}/traffic/clones")
    }

    def getIssue(owner = this.owner, repo = this.repo, int issueNumber) {
        getJson("repos/${owner}/${repo}/issues/${issueNumber}")
    }

    def getIssueComments(owner = this.owner, repo = this.repo, issueNumber) {
        getJson("repos/${owner}/${repo}/issues/${issueNumber}/comments")
    }

    private getJson(endpoint) {
        Map authHeaders = token ? [Authorization: "token $token"] : [:]
        Map headers = [Accept: 'application/json'] + authHeaders
        def respBody = "$BASE_URL/$endpoint".toURL().getText(requestProperties: headers)

        new JsonSlurper().parseText(respBody)
    }
}
