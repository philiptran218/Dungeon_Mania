package dungeonmania.StaticEntities;

public class Wall extends StaticEntity {
    public Wall() {
        super.setCanStandOn(false);
        super.setType("Wall");
    }
}
