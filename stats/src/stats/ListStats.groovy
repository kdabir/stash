package stats

class ListStats {

    @Delegate
    final List list

    ListStats(List list) {
        this.list = list.sort(false)
    }

    def mean() {
        list.sum() / list.size()
    }

    def median() {
        def size = list.size() // 8 or 9
        def center = (size / 2) as Integer// 4 or 4.5 => 4
        (size % 2 == 0) ? (list[center - 1] + list[center]) / 2 : list[center]
    }

    def sampleVariance() {
        def mean = mean()
        list.collect { num -> Math.pow(num - mean, 2) }.sum() / (list.size() - 1)
    }

    def populationVariance() {
        def mean = mean()
        list.collect { num -> Math.pow(num - mean, 2) }.sum() / (list.size())
    }

    def sampleStandardDeviation() {
        Math.sqrt(sampleVariance())
    }

    def populationStandardDeviation() {
        Math.sqrt(populationVariance())
    }
}
