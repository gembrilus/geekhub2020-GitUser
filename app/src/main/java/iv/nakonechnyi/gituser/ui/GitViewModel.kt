package iv.nakonechnyi.gituser.ui

import android.util.Log
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
                    Log.i("GitViewModel", "Success")
                    _success.value = status.result
                }
                is Status.Failed -> failed.value = status.error
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