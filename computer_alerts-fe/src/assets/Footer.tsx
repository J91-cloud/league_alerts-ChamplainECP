import styles from "../assets/Footer.module.css";
import LeagueImage from "../assets/LeagueAlertsImg.jpg";

export default function Footer() {
  return (
    <div className={styles.container}>
      <div className={styles.imgContainer}>
        <img src={LeagueImage} alt="League Alerts" className={styles.img} />
      </div>
      <div className={styles.textContainer}>
        <p className={styles.text}>This is your footer</p>
      </div>
    </div>
  );
}
