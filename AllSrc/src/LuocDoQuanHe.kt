class LuocDoQuanHe(leftList: MutableList<String>, var u: String, rightList: MutableList<String>) {
    val left = leftList
    val right = rightList
    var functionalDependency = mutableListOf<MutableList<String>>(leftList, rightList)

    var allSubstring: MutableList<String> = mutableListOf()
    fun findClosure(source:String, fDs: MutableList<MutableList<String>>):String{
        var isStillChangeAble = true
        var closure = source
        while (isStillChangeAble){
            isStillChangeAble = false;
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
        var attributeArray = attribute.toCharArray()
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
        var sizeOfSet = functionalDependency[0].size
        for(temp in 0 until sizeOfSet){
            var subString = findAllSubString(functionalDependency[0][temp])
            for (temp2 in subString){
                if(isContains(findClosure(temp2, functionalDependency), functionalDependency[1][temp])){
                    functionalDependency[0][temp] = temp2
                    break
                }
            }
        }
    }
     fun findAllSubString(source: String): MutableList<String>{
         findsubsequences(source, "")
         return allSubstring
     }
    fun findsubsequences(s: String, ans: String){
        if(s.length==0){
            allSubstring.add(ans)
            return
        }
        findsubsequences(s.substring(1), ans+ s.get(0))
        findsubsequences(s.substring(1), ans)
    }

    fun removeRedundantFunctional(){
        for(temp in 0 until functionalDependency[0].size){
            var tempAttribute = functionalDependency[0][temp]
            functionalDependency[0][temp] = "null"
            if(isContains(findClosure(tempAttribute, functionalDependency), functionalDependency[1][temp]))
                functionalDependency[1][temp] = "null"
            else
                functionalDependency[0][temp] = tempAttribute
        }
        functionalDependency[0].removeIf{String-> String.contentEquals("null")}
        functionalDependency[1].removeIf{String-> String.contentEquals("null")}
    }

    fun findCandidateKey(){
        var result: MutableList<String> = mutableListOf()
        var r = mutableLisToString(functionalDependency[1])
        var s = setDifference(u, r)
        if (isEqual(u, findClosure("s", functionalDependency))) {
            println("Chỉ có 1 candidate key là $s")
            return
        }
        var l = mutableLisToString(functionalDependency[0])
        var lIntersectionR = intersection(l, r)
        for (temp in findAllSubString(lIntersectionR)){
            var attributeNeedCheck = temp + s
            if (isEqual(u, findClosure(attributeNeedCheck, functionalDependency))){
                result.add(attributeNeedCheck)
            }
        }
        println(result)
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
        var sArray = s.toCharArray()
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
        var sourceArray = source.toCharArray()
        for (temp in sourceArray){
            if(!isContains(attribute, temp.toString()))
                result+=temp.toString()
        }
        return result
    }
    fun output(fDs: MutableList<MutableList<String>>){
        for(i in 0 until fDs[0].size){
            println("${fDs[0][i]} -> ${fDs[1][i]}")
        }
    }
}