package at.technikum.application.mrp.leaderboard;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class LeaderboardService {

    private final LeaderboardRepositoryC leaderboardRepositoryC;

    public LeaderboardService(LeaderboardRepositoryC leaderboardRepositoryC) {
        this.leaderboardRepositoryC = leaderboardRepositoryC;
    }

    public Leaderboard create(Leaderboard leaderboard) {
        // is todo valid?
        return null;
    }

    public Leaderboard get(String id) {
        return leaderboardRepositoryC.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Leaderboard> getAll() {
        System.out.println("in getAll_Service");
        List<Leaderboard> lb = leaderboardRepositoryC.findAll();
        /*
        boolean clean = true;
        while(!clean){
            for(int i = 0; i<lb.size()-1; i++){
                if(lb.get(i).ratingAnzahl<lb.get(i+1).getRatingAnzahl()){
                    lb_2 = lb.get(i);
                    lb.get(i) = lb.get(i+1);
                    lb.sort(lb.);
                }
            }
        }

         */
        System.out.println("in getAll_Service_before_Sort");
        lb.sort(Comparator.comparingInt(Leaderboard::getRatingAnzahl).reversed());  // Größte Zahl zuerst
        System.out.println("in getAll_Service_after_Sort");
        return lb;
    }

    public Leaderboard update(String Title, Leaderboard update) {
        /*
        Leaderboard leaderboard = leaderboardRepositoryC.find(update.getTitle())
                .orElseThrow(EntityNotFoundException::new);

        leaderboard.setTitle(update.getTitle());
        //leaderboard.setDone(update.isDone());

        return leaderboardRepositoryC.save(leaderboard);
    }

    public Leaderboard delete(String title) {
        return leaderboardRepositoryC.delete(title);
    }
         */
        return null;
    }
}