package baikal.web.footballapp;

import baikal.web.footballapp.model.ActiveMatches;
import baikal.web.footballapp.model.AddTeam;
import baikal.web.footballapp.model.Advertisings;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.Announces;
import baikal.web.footballapp.model.Clubs;
import baikal.web.footballapp.model.DataClub;
import baikal.web.footballapp.model.EditCommand;
import baikal.web.footballapp.model.EditCommandResponse;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.EditProtocolBody;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Matches;
import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.People;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.RefereeRequestList;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.ServerResponse;
import baikal.web.footballapp.model.SetRefereeList;
import baikal.web.footballapp.model.SignIn;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FootballApi {
    //get all news
    @GET("/api/news")
    Call<News> getAllNews(@Query("limit") String limit, @Query("offset") String offset);

    @GET("/api/crud/news")
    Call<List<News_>> getAllNewsCrud(@Query("_limit") String limit, @Query("_offset") String offset);

    //get all news
    @GET("/api/crud/announce")
    Observable<List<Announce>> getAllAnnounce(@Query("_limit") String limit, @Query("_offset") String offset);

    //get advertising
    @GET("/api/ads")
    Observable<Advertisings> getAdvertising(@Query("limit") String limit, @Query("offset") String offset);



    //get all clubs
    @GET("/api/clubs")
    Observable<Clubs> getAllClubs();
//    Call<Clubs> getAllClubs();





    //get tournament's info
    //@GET("/api/leagues/league/{id}")
    @GET("/api/crud/league")
    Observable<GetLeagueInfo> getLeagueInfo(@Query("_id") String id);
//    Call<GetLeagueInfo> getLeagueInfo(@Path("id") String id);



    //get active matches
    @GET("/api/matches/active")
    Observable<ActiveMatches> getActiveMatches(@Query("limit") String limit, @Query("offset") String  offset,
                                         @Query("played") Boolean played);


    //get coming matches
    @GET("/api/matches/upcoming")
    Observable<ActiveMatches> getComingMatches();




    //get referees type==referee
    //get players type==player
    @GET("/api/getusers")
    Observable<People> getAllUsers(@Query("type") String type, @Query("search") String search, @Query("limit") String limit, @Query("offset") String offset);
    @GET("/api/crud/person")
    Observable<List<Person> >getAllPersons(@Query("type") String type, @Query("surname") String surname,@Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/tourney")
    Observable<List<Tourney>> getTourneys(@Query("name") String name, @Query("region") String region);
    //get all tournaments
//    @GET("/api/leagues/all")
//    Observable<Tournaments> getAllTournaments( @Query("limit") String limit, @Query("offset") String offset);
//    //    Call<Tournaments> getAllTournaments();
    @GET("/api/crud/league?_populate=matches?_populate=stages")
    Observable<List<League>> getAllLeagues(@Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/league")
    Call<List<League>> getLeaguesByTourney(@Query("tourney") String tourney);
    //edit web
    @Multipart
    @POST("/api/editPlayerInfo")
    Call<EditProfile> editProfile(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
    @Multipart
    @PATCH("/api/crud/person/{id}")
    Call<EditProfile> editPlayerInfo(@Path("id") String id, @Header("auth") String authorization, @Part("favoriteTourney") List<RequestBody> favoriteTourney);
    //edit web profile
    @POST("/api/editPlayerInfo")
    Call<EditProfile> editProfileText(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    @GET("/api/crud/person?_populate=favoriteTourney&_select=favoriteTourney")
    Call<List<PersonPopulate> >getFavTourneysByPerson(@Query("_id") String id);

    //edit match protocol event and playerList
    @POST("/api/matches/changeProtocol")
    Call<Matches> editProtocol(@Header("auth") String authorization, @Body EditProtocolBody body);

    //edit match protocol referees
    @POST("/api/matches/setreferees")
    Observable<Response<Matches>> editProtocolReferees(@Header("auth") String authorization, @Body RefereeRequestList body);


    //confirm protocol
    @FormUrlEncoded
    @POST("/api/matches/acceptProtocol")
    Observable<Response<ServerResponse>> confirmProtocol(@Header("auth") String authorization, @Field("_id") String id);

    //set referee
    @POST("/api/matches/setmultireferees")
    Observable<Response<ServerResponse>> setReferees(@Header("auth") String authorization, @Body SetRefereeList body);


    //refresh web
    @GET("/api/refresh")
    Observable<User> refreshUser(@Header("auth") String authorization);

    //sign up web
    @Multipart
    @POST("/api/signup")
    Call<User> signUp(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);


    //sign in web
    @POST("/api/signin")
    Call<User> signIn(@Body SignIn body);

    //add player to team
    @Multipart
    @POST("/api/team/addplayer")
    Call<ServerResponse> addPlayerToTeam(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    //create new team
    @Multipart
    @POST("/api/leagues/addrequest")
    Call<AddTeam> addTeam(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    //edit team
    @POST("/api/team/edit")
    Call<EditCommandResponse> editTeam(@Header("auth") String authorization, @Body EditCommand body);

    //player inv
    @Multipart
    @POST("/api/team/acceptrequest")
    Call<User> playerInv(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);


    //get all player's teams
    @GET("/api/team/teamsbyid")
    Call<Team> getTeam(@Query("_id") String id);

    //add new club
    @Multipart
    @POST("/api/clubs/add")
    Call<DataClub> addClub(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    //edit club
    @Multipart
    @POST("/api/clubs/edit")
    Call<DataClub> editClub(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @GET("/api/crud/region")
    Call<List<Region>> getRegions();
    @GET("/api/crud/tourney")
    Call<List<Tourney>> getAllTourneys();
}

