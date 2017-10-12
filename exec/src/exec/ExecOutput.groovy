package exec

import groovy.transform.Immutable
import groovy.transform.ToString

@Immutable
@ToString(includeNames=true)
class ExecOutput {
    String out, err
    Integer exitValue
}


