package gh

import spock.lang.Requires
import spock.lang.Specification

class GitHubTest extends Specification {

    static class Keys {
        static def githubAccessToken = System.properties['github.accessToken']
    }

    def "list repos"() {
        expect:
        new GitHub(owner: 'kdabir').getReposNames().contains('glide')
    }

    def "get issues"() {
        def issue = new GitHub(owner: 'kdabir', repo: 'glide').getIssue(1)

        expect:
        issue != null
    }

    @Requires({ Keys.githubAccessToken })
    def "get traffic"() {
        def traffic = new GitHub(owner: 'kdabir', repo: 'glide', token: Keys.githubAccessToken).getTraffic()

        expect:
        traffic != null
    }

}
