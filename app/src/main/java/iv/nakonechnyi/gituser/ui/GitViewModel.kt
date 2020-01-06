package iv.nakonechnyi.gituser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.repository.Status
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import kotlinx.coroutines.launch

class GitViewModel(
    private val repository: GitUserRepository
) : ViewModel() {

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _position

    fun setPosition(value: Int){
        _position.value = value
    }

    private val _success = MutableLiveData<List<GitUserWithRepos>>()
    val success: LiveData<List<GitUserWithRepos>> get() = _success

    fun removeItem(login: String) {
        viewModelScope.launch {
            repository.removeUserFromDb(login)
        }
    }

    val failed = MutableLiveData<Throwable>()

    val noNetwork = MutableLiveData<Boolean>()

    fun refresh(login: String?){
        viewModelScope.launch {

            when(val status = repository.getFullUserInfo(login)){
                is Status.Success -> {
                    _success.value = status.result
                    noNetwork.value = false
                }
                is Status.DbSuccess -> {
                    _success.value = status.result
                    noNetwork.value = true
                }
                is Status.Failed -> {
                    failed.value = status.error
                    noNetwork.value = false
                }
                is Status.NetworkFailed -> {
                    _success.value = status.result
                    noNetwork.value = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.closeDb()
    }
}