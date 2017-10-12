package exec

/**
 * Exec
 *
 * largely inspired by  : https://stackoverflow.com/questions/159148/groovy-executing-shell-commands
 */
class Exec {
    static ExecOutput blocking(final Map userOpts = [:], String command) {
        final Map defaultOpts = [wait: 1000L, verbose: false]
        final Map opts = defaultOpts + userOpts

        StringBuilder sout = new StringBuilder(),
                      serr = new StringBuilder()

        def process = command.execute()
        process.consumeProcessOutput(sout, serr)
        process.waitForOrKill(opts.wait as int)

        new ExecOutput(out: sout.toString(), err: serr.toString(), exitValue: process.exitValue())
    }
}
