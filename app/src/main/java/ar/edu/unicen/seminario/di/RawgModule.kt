package ar.edu.unicen.seminario.di

import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.ddl.data.Rawg.RawgApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class RawgModule {
    @Provides
    fun provideRawgApi(retrofit: Retrofit): RawgApi {
        return retrofit.create(RawgApi::class.java)
    }

    @Provides
    fun provideRawgRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Named("rawgApiKey")
    fun provideRawgApiKey(): String {
        return BuildConfig.RAWG_API_KEY
    }
}
