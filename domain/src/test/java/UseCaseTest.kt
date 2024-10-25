
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.AddTaskUseCase
import com.example.domain.usecase.MarkTaskCompletedUseCase
import com.example.domain.usecase.GetAllCurrentTasksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class UseCaseTest {
    private val repository = mock<TaskRepository>()
    @AfterEach
    fun tearDown(){
        Mockito.reset(repository)
    }
    @Test
    fun `should invoke add method`(){
        val useCase = AddTaskUseCase(repository)

        val testTask = Task(title = "testTask title")
        runBlocking {
            useCase.execute(newTask = testTask)
            Mockito.verify(repository, times(1)).addTask(testTask)
        }
    }
    @Test
    fun `should invoke delete method`(){
        val useCase = MarkTaskCompletedUseCase(repository)

        val testTask = Task(title = "testTask title")
        runBlocking {
            useCase.execute(task = testTask)
            Mockito.verify(repository, times(1)).markCompleted(testTask)
        }
    }
    @Test
    fun `should receive all tasks`(){
        val useCase = GetAllCurrentTasksUseCase(repository)

        val expected = flowOf(
            listOf(
                Task(title = "testTask title 1"),
                Task(title = "testTask title 2"),
                Task(title = "testTask title 3"),
                Task(title = "testTask title 4"),
                Task(title = "testTask title 5")
            )
        )
        Mockito.`when`(repository.getAllCurrentTasks()).thenReturn(expected)

        val actual: Flow<List<Task>>

        runBlocking {
            actual = useCase.execute()
        }
        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAllCurrentTasks()
    }
}