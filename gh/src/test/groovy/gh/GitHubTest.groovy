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

    def "get starred repos"() {
        def stars = new GitHub(owner: 'kdabir').getStarredRepos()

        expect: 'default results size is 10'
        stars.size() == 10
    }

    def "get starred repos opts work"() {
        def fields = ['full_name', 'description', 'language', 'stargazers_count', 'forks_count','open_issues_count', 'created_at']
        def stars = new GitHub(owner: 'kdabir').getStarredRepos(per_page:3, fields:fields)

        expect: 'return limited results and fields'
        stars.size() == 3
        stars[1].size() <= fields.size()
    }

    @Requires({ Keys.githubAccessToken })
    def "get traffic"() {
        def traffic = new GitHub(owner: 'kdabir', repo: 'glide', token: Keys.githubAccessToken).getTraffic()

        expect:
        traffic != null
    }

}
