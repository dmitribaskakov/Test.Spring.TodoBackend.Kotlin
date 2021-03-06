package org.home.todobackend.entity

import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Cache
import java.util.HashSet
import java.util.Objects
import javax.persistence.*

/*
пользователь - основной объект, с которым связаны все остальные (через внешние ключи)
*/
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
/** Стратегия кеширования
 * READ_ONLY – Самый быстрый из всех, т.к. отключены доп. проверки при работе с кешем.
 * Используется для получения постоянных справочных данных, которые никогда не меняются.
 * Если в БД обновится запись – в кеше все равно останется устаревшая версия (пока кеш не очистится и вы заново не получите данные из БД).
 *
 * NONSTRICT_READ_WRITE – Не гарантирует сильной согласованности между кешем и базой данных, но уже хотя бы какая-то синхронизация.
 * Кэш синхронизируется с БД после коммита транзакции.
 * Если в момент обновления приложение запросить данные из кеша – он получит устаревшие данные.
 * Поэтому может появиться небольшое временное окно, в котором вы будете получать из кеша устаревшие данные.
 * Используйте, если данные почти никогда не изменяются.
 * Также если для вас не сильно критично, что иногда будете получать из кеша устаревшие значения.
 *
 * READ_WRITE – Более строгий вариант и более сильная синхронизация с БД, чем NONSTRICT_READ_WRITE.
 * Используется в случае, когда данные в БД обновляются часто и вам нужно, чтобы кеш всегда поддерживался в актуальном состоянии.
 * При синхронизации entity в кэше с БД – этот объект “мягко блокируется” и освобождается после коммита транзакции.
 * Если в этот момент синхронизации приложение запросит объект из кеша – ему придется выполнить запрос в БД напрямую, т.к. объект заблокирован.
 * Таким образом приложение всегда получает актуальные данные (либо из кеша, либо из БД)
 *
 * TRANSACTIONAL – Самый строгий и медленный из всех.
 * Если в момент синхронизации кеша и БД – приложение запросит этот объект из кеша – оно будет ждать, пока блокировка не будет снята (транзакция завершится и данные полностью синхронизируются с БД)
 *
 * NONE – отключение – entity не будет кешироваться
 */
@Entity
@Table(name = "user_data", schema = "todolist")
class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val email: String? = null
    val username: String? = null

    @Column(name = "userpassword", nullable = false, length = -1)
    val password: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    val roles: Set<Role> = HashSet()

    //    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //    private List<Task> tasks;
    //
    //    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //    private List<Category> categories;
    //
    //    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //    private List<Priority> priorities;
    //
    //    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    //    private Activity activity;
    //
    //    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    //    private Stat stat;
    override fun toString(): String {
        return email!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return id == user.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}