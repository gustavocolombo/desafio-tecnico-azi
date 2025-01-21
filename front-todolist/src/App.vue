<template>
  <v-app theme="dark">
    <Header @search-task="fetchTaskById" />
    <v-main>
      <v-container
        class="d-flex flex-column justify-center align-items-center mt-5"
      >
        <div class="d-flex align-items-center mt-12">
          <h1 class="text-center mr-auto">Minhas Tarefas</h1>
          <v-btn style="background-color: #424242" @click="openCreateModal">
            <span>Criar tarefa</span>
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>
        <TaskList
          :tasks="tasks"
          @edit-task="openEditModal"
          @update-task="updateTask"
          @delete-task="deleteTask"
        />
      </v-container>

      <ModalCreateTask v-model="isOpenModal" @save-task="createTask" />
      <ModalUpdateTask
        v-model="editModal"
        :task="selectedTask"
        @save-task="updateTask"
      />
    </v-main>
  </v-app>
</template>

<script>
import { ref } from "vue";
import axios from "axios";
import TaskList from "./components/TaskList.vue";
import ModalCreateTask from "./components/ModalCreateTask.vue";
import ModalUpdateTask from "./components/ModalUpdateTask.vue";
import { toast } from "vue3-toastify";
import "vue3-toastify/dist/index.css";

export default {
  components: { TaskList, ModalCreateTask, ModalUpdateTask },

  setup() {
    const tasks = ref([]);
    const isOpenModal = ref(false);
    const selectedTask = ref({});
    const editModal = ref(false);

    const fetchTasks = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/tasks");
        tasks.value = response.data;
      } catch (error) {
        console.error("Failed at get tasks", error);
      }
    };

    const fetchTaskById = async (taskId) => {
      try {
        await axios.get(`http://localhost:8080/api/v1/tasks/${taskId}`);
        toast.success("Tarefa encontrada com sucesso");
      } catch (error) {
        console.error("Failed at get task", error);
        toast.error("Tarefa não encontrada");
      }
    };

    const openCreateModal = () => {
      isOpenModal.value = true;
    };

    const closeCreateModal = () => {
      isOpenModal.value = false;
    };

    const createTask = async (newTask) => {
      try {
        const response = await axios.post(
          "http://localhost:8080/api/v1/tasks",
          newTask
        );

        tasks.value.push(response.data);
        closeCreateModal();
        toast.success("Tarefa salva com sucesso");
      } catch (error) {
        console.error("Failed at create task", error);
        toast.error("Falha ao criar a tarefa, o título não pode ser vazio");
      }
    };

    const openEditModal = (task) => {
      selectedTask.value = { ...task };
      editModal.value = true;
    };

    const updateTask = async (updatedTask) => {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/v1/tasks/${updatedTask.id}`,
          updatedTask
        );

        const index = tasks.value.findIndex(
          (task) => task.id === updatedTask.id
        );

        if (index !== -1) {
          tasks.value[index] = response.data;
        }

        editModal.value = false;
        toast.success("Tarefa atualizada com sucesso");
      } catch (error) {
        console.error("Failed at update task", error);
        toast.error("Falha ao atualizar a tarefa");
      }
    };

    const deleteTask = async (taskId) => {
      try {
        await axios.delete(`http://localhost:8080/api/v1/tasks/${taskId}`);
        tasks.value = tasks.value.filter((task) => task.id !== taskId);
        toast.success("Tarefa deletada com sucesso");
      } catch (error) {
        console.error("Failed at delete task", error);
        toast.error("Falha ao deletar a tarefa");
      }
    };

    fetchTasks();

    return {
      tasks,
      isOpenModal,
      openCreateModal,
      createTask,
      openEditModal,
      updateTask,
      editModal,
      selectedTask,
      deleteTask,
      fetchTaskById,
    };
  },
};
</script>
