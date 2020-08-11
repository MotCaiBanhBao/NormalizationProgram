class LuocDoQuanHe(leftList: MutableList<String>, private var u: String, rightList: MutableList<String>) {
    private val left = leftList
    private val right = rightList
    var functionalDependency = mutableListOf(left, right)


    private fun findClosure(source:String, fDs: MutableList<MutableList<String>>):String{
        var isStillChangeAble = true
        var closure = source
        while (isStillChangeAble){
            isStillChangeAble = false
            for(index in 0 until fDs[0].size)
                if(isContains(closure, fDs[0][index])){
                    val addAttribute = fDs[1][index]
                    if(!isContains(closure, addAttribute)){
                        closure = addAttribute(closure, addAttribute)
                        isStillChangeAble = true
                    }
                }
        }
        return closure
    }
     private fun addAttribute(source: String, attribute: String): String{
        var result = source
        val attributeArray = attribute.toCharArray()
        for(oneAttribute in attributeArray)
            if(!result.contains(oneAttribute))
                result += oneAttribute
        return result
    }
     private fun isContains(Container: String, needCheckString: String): Boolean{
        val needCheckToArray = needCheckString.toCharArray()
        for(temp in needCheckToArray)
            if(!Container.contains(temp))
                return false
        return true
    }
    fun findMinimalCover(){
        var sizeOfMap = functionalDependency[0].size
        var temp = 0
        while (temp<sizeOfMap){
            if(functionalDependency[1][temp].length>1){
                convertRHS(temp)
                temp-=1
                sizeOfMap = functionalDependency[0].size
            }
            temp++
        }
        removeDuplicate()
    }
    private fun convertRHS(index: Int){
        for(temp in (functionalDependency[1][index]).toCharArray()){
            functionalDependency[0].add(functionalDependency[0][index])
            functionalDependency[1].add(temp.toString())
        }
        functionalDependency[0].removeAt(index)
        functionalDependency[1].removeAt(index)
    }
    private fun removeDuplicate(){
        var sizeOfSet = functionalDependency[0].size
        for (temp in 0 until sizeOfSet-2){
            for(temp2 in temp+1 until sizeOfSet-1){
                if(functionalDependency[0][temp] == functionalDependency[0][temp2]
                        && functionalDependency[1][temp] == functionalDependency[1][temp2]){
                    functionalDependency[0].removeAt(temp2)
                    functionalDependency[1].removeAt(temp2)
                    sizeOfSet = functionalDependency[0].size
                }
            }
        }
    }
    fun findLHSExtraneous(){
        val sizeOfSet = functionalDependency[0].size
        for(temp in 0 until sizeOfSet){
            val subString = findAllSubString(functionalDependency[0][temp])
            for (temp2 in subString){
                if(isContains(findClosure(temp2, functionalDependency), functionalDependency[1][temp])){
                    functionalDependency[0][temp] = temp2
                    break
                }
            }
        }
    }
    private fun findAllSubString(source: String): MutableList<String>{
        val allSubstring: MutableList<String> = mutableListOf()
        findSubSequences(source, "", allSubstring)
        allSubstring.removeAt(allSubstring.size-1)
        allSubstring.sortBy { it.length }
        for(i in 0 until allSubstring.size){
            if(allSubstring[i].length==source.length)
                allSubstring.removeAt(i)
        }
         return allSubstring
     }
    private fun findSubSequences(s: String, ans: String, allSubstring: MutableList<String>){
        if(s.isEmpty()){
            allSubstring.add(ans)
            return
        }
        findSubSequences(s.substring(1), ans+ s[0], allSubstring)
        findSubSequences(s.substring(1), ans, allSubstring)
    }

    fun removeRedundantFunctional(){
        for(temp in 0 until functionalDependency[0].size){
            val tempAttribute = functionalDependency[0][temp]
            functionalDependency[0][temp] = "null"
            if(isContains(findClosure(tempAttribute, functionalDependency), functionalDependency[1][temp]))
                functionalDependency[1][temp] = "null"
            else
                functionalDependency[0][temp] = tempAttribute
        }
        functionalDependency[0].removeIf{String-> String.contentEquals("null")}
        functionalDependency[1].removeIf{String-> String.contentEquals("null")}
    }

    fun findCandidateKey(): MutableList<String>{
        val result: MutableList<String> = mutableListOf()
        val r = mutableLisToString(functionalDependency[1])
        val s = setDifference(u, r)
        if (isEqual(u, findClosure(s, functionalDependency))) {
            result.add(s)
            return result
        }
        val l = mutableLisToString(functionalDependency[0])
        val lIntersectionR = intersection(l, r)
        for (temp in findAllSubString(lIntersectionR)){
            val attributeNeedCheck = temp + s
            if (isEqual(u, findClosure(attributeNeedCheck, functionalDependency))){
                if (isSubElementIn(result, attributeNeedCheck))
                    result.add(attributeNeedCheck)
            }
        }
        return result
    }
    private fun isSubElementIn(source: MutableList<String>, attribute: String): Boolean{
        for (tempResult in source){
            if (isContains(attribute, tempResult))
                return false
        }
        return true
    }
    private fun isEqual(source: String, needCheck: String): Boolean{
        val sourceArray = source.toCharArray()
        for (temp in sourceArray)
            if (!isContains(needCheck, temp.toString()))
                return false
        return true
    }
    private fun intersection(r:String, s:String): String{
        var result = ""
        val sArray = s.toCharArray()
        for (temp in sArray){
            if(isContains(r, temp.toString()))
                result+=temp.toString()
        }
        return result
    }
    private fun mutableLisToString(source: MutableList<String>): String{
        var result = ""
        for (temp in source)
            if (!isContains(result, temp))
                result+=temp
        return result
    }
    private fun setDifference(source: String, attribute: String): String{
        var result = ""
        val sourceArray = source.toCharArray()
        for (temp in sourceArray){
            if(!isContains(attribute, temp.toString()))
                result+=temp.toString()
        }
        return result
    }

    fun checkForm(){
        println(if(is2NF()) "Là 2 NF" else "Không phải là 2NF")
        println(if(is3NF()) "Là 3 NF" else "Không phải là 3NF")
        println(if(isBCNF()) "Là BCNF" else "Không phải là BCNF")
    }
    private fun is2NF(): Boolean{
        val allKey = findCandidateKey()
        val allKeyString = allKey.toString()
        val notPrimeKey = setDifference(u, allKeyString)
        for (temp in allKey){
            val subString = findAllSubString(temp)
            println(temp)
            println(subString)
            for (tempSubString in subString){
                if (isContains(findClosure(tempSubString, functionalDependency), notPrimeKey))
                    return false
            }
        }
        return true
    }
    private fun is3NF(): Boolean{
        val allKey = findCandidateKey()
        val nonKey = setDifference(u, allKey.toString())
        for (temp in 0 until functionalDependency[0].size){
            if (!isEqual(u, findClosure(functionalDependency[0][temp], functionalDependency))|| isContains( functionalDependency[1][temp], nonKey)){
                return false
            }
        }
        return true
    }
    private fun isBCNF(): Boolean{
        for (temp in 0 until functionalDependency[0].size){
            if (!isEqual(u, findClosure(functionalDependency[0][temp], functionalDependency)))
                return false
        }
        return true
    }
    fun output(fDs: MutableList<MutableList<String>>){
        for(i in 0 until fDs[0].size){
            println("${fDs[0][i]} -> ${fDs[1][i]}")
        }
    }
}