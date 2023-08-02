package com.example.gezdir.ui.component.home.fragment.discover

import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.data.repo.DataRepository
import com.example.gezdir.util.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor ( val dataRepo : DataRepository): ViewModel() {

    fun getMostPopularCitiyList(onComplete: (List<Advert>) -> Unit) {
        return dataRepo.getMostPopularCityList(){
            onComplete(it)
        }
    }

    fun getProfileUrl(imagePath:String){
        return dataRepo.getImageUrl(imagePath){
            UserManager.currentUserProfileUrl = it
        }
    }

    fun isThereANotification(onComplete: (Boolean) -> Unit) {
        return dataRepo.isThereANotification(){
            onComplete(it)
        }
    }

}